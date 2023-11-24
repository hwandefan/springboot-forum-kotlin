package com.hwandefan.forum.service.user

import com.hwandefan.forum.model.User
import com.hwandefan.forum.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
public class UserService @Autowired constructor(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return userRepository.findByEmail(username!!)!!
    }

    fun updateUser(username: String, params:ArrayList<String?>):String? {
        return try {
            val user:User = loadUserByUsername(username) as User
            if(params[0] != null)
                user.preferences.userStatus = params[0]
            if(params[1] != null)
                user.firstName = params[1]!!
            if(params[2] != null)
                user.lastName = params[2]!!
            if(params[3] != null)
                user.preferences.profilePhoto = photoLinkGeneration(params[3]!!, user.username)
            userRepository.save(user)
            "OK"
        } catch (e:Exception) {
            null
        }
    }

    private fun photoLinkGeneration(base64String: String, username:String):String {
        val base64Bytes = Base64.getDecoder().decode(base64String)
        val publicPathFile = "/profile_photos/${username}_main_photo.png"
        val pathFile = "src/main/resources/public${publicPathFile}"
        val path = Paths.get(pathFile)
        Files.write(path,base64Bytes)
        return publicPathFile
    }

    fun deleteUser(username: String):Boolean {
        val user:User = loadUserByUsername(username) as User
        return try {
            userRepository.delete(user)
            Files.deleteIfExists(Paths.get(
                "src/main/resources/public/profile_photos/${username}_main_photo.png"))
            true
        } catch (e:Exception) {
            false
        }
    }
}