package com.hwandefan.forum.api.admin

import com.fasterxml.jackson.databind.ObjectMapper
import com.hwandefan.forum.model.Role
import com.hwandefan.forum.model.User
import com.hwandefan.forum.model.UserPreferences
import com.hwandefan.forum.service.user.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
internal class AdminControllerTest {
    @Autowired
    private lateinit var mockMvc:MockMvc

    @MockBean
    private lateinit var userService: UserService

    private val objectMapper = ObjectMapper()
    private val mockUsersArray = arrayListOf(
        UserResponse(
            "test@test.com",
            "1234-1234-4321-4321",
            Role.USER,
            "test name",
            "test lastname",
            null
        ),
        UserResponse(
            "test2@test.com",
            "4321-4321-4321-4321",
            Role.ADMIN,
            "test name2",
            "test lastname2",
            "src/to/image"
        ),
        UserResponse(
            "test3@test.com",
            "1234-1234-1234-1234",
            Role.USER,
            "test name3",
            "test lastname3",
            null
        )
    )
    private val mockUser = User(
        UUID.randomUUID(),
        "Test User Name",
        "Test User Surname",
        "test",
        "test",
        Role.ADMIN,
        UserPreferences()
    )

    @Test
    fun `should return the list of all users`() {
        `when`(userService.getAllUsers())
            .thenReturn(mockUsersArray)
        mockMvc.get("/api/v1/admin/all_users") {
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(mockUsersArray))
                }
            }
    }

    @Test
    fun `should delete the user by id`() {
        val userId = "1234-1234-4321-4321"
        val userResponse = UserInfoResponse(userId, "User is deleted")
        `when`(userService.deleteUserById(userId))
            .thenReturn(userResponse)
        mockMvc.delete("/api/v1/admin/delete_user/${userId}") {
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(userResponse))
                }
            }
    }

    @Test
    fun `should give admin rights to the user`() {
        val userId = "1234-1234-4321-4321"
        val userResponse = UserInfoResponse(userId, "Admin role is granted")
        `when`(userService.grantAdminRights(userId))
            .thenReturn(userResponse)
        mockMvc.post("/api/v1/admin/grant_admin_to_user/${userId}"){
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(userResponse))
                }
            }
    }
}