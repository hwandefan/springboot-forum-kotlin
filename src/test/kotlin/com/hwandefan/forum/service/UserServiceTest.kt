package com.hwandefan.forum.service

import com.hwandefan.forum.model.Role
import com.hwandefan.forum.model.User
import com.hwandefan.forum.model.UserPreferences
import com.hwandefan.forum.repository.user.UserRepository
import com.hwandefan.forum.service.user.UserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    @InjectMocks
    private lateinit var userService: UserService

    @Mock
    private lateinit var userRepository: UserRepository

    private val userId = UUID.randomUUID()
    private val userEmail = "my_test_name@test.com"
    private val mockUsers = listOf(
        User(
            UUID.randomUUID(),
            "Test User Name",
            "Test User Surname",
            "test1",
            "test",
            Role.USER,
            UserPreferences()
        ),
        User(
            UUID.randomUUID(),
            "Test User Name",
            "Test User Surname",
            "test2",
            "test",
            Role.USER,
            UserPreferences()
        ),
        User(
            userId,
            "Test User Name",
            "Test User Surname",
            userEmail,
            "test",
            Role.USER,
            UserPreferences()
        )
    )

    @Test
    fun `should return user by email`() {
        `when`(userRepository.findByEmail(userEmail))
            .thenReturn(mockUsers[2])
        userService.loadUserByUsername(userEmail)
        verify(userRepository).findByEmail(userEmail)
    }

    @Test
    fun `should update the user`() {
        `when`(userRepository.findByEmail(userEmail))
            .thenReturn(mockUsers[2])
        val imageOneXOnePixelInStringForBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABAQMAAAAl21bKAAAAA" +
                "1BMVEUAAACnej3aAAAAAXRSTlMAQObYZgAAAApJREFUCNdjYAAAAAIAAeIhvDMAAAAASUVORK5CYII="
        userService.updateUser(
            userEmail, arrayListOf(null,null,null,imageOneXOnePixelInStringForBase64)
        )
        val userCaptor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository).save(userCaptor.capture())
        val capturedUser = userCaptor.value
        val dataBaseImagePath = "/profile_photos/${userEmail}_main_photo.png"
        assert(capturedUser.getUserId() == userId)
        assert(capturedUser.firstName == mockUsers[2].firstName)
        assert(capturedUser.preferences.profilePhoto == dataBaseImagePath)
        removeTestedImage()
    }

    private fun removeTestedImage() {
        Files.deleteIfExists(
            Paths.get("src/main/resources/public/profile_photos/${userEmail}_main_photo.png"))
    }

    @Test
    fun `should grant admin rights to user`() {
        `when`(userRepository.findById(userId))
            .thenReturn(Optional.of(mockUsers[2]))
        userService.grantAdminRights(userId.toString())
        val userCaptor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository).save(userCaptor.capture())
        val capturedUser = userCaptor.value
        assert(capturedUser.getRole() == Role.ADMIN)
    }

    @Test
    fun `should delete the user by email`() {
        `when`(userRepository.findByEmail(userEmail))
            .thenReturn(mockUsers[2])
        userService.deleteUserByUsername(userEmail)
        verify(userRepository).delete(Mockito.any(User::class.java))
    }

    @Test
    fun `should delete the user by user id`() {
        `when`(userRepository.findById(userId))
            .thenReturn(Optional.of(mockUsers[2]))
        userService.deleteUserById(userId.toString())
        verify(userRepository).delete(mockUsers[2])
    }

    @Test
    fun `should return all users`() {
        `when`(userRepository.findAll())
            .thenReturn(mockUsers)
        val response = userService.getAllUsers()
        assert(response.size == 3)
        assert(response[2].email == mockUsers[2].username)
    }
}