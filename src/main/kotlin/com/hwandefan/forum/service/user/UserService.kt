package com.hwandefan.forum.service.user

import com.hwandefan.forum.model.User
import com.hwandefan.forum.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.lang.Exception

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
                user.preferences.profilePhoto = params[3]
            userRepository.save(user)
            "OK"
        } catch (e:Exception) {
            null
        }
    }
}