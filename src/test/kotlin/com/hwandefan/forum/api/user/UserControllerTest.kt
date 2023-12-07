package com.hwandefan.forum.api.user

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
import org.springframework.test.web.servlet.put
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    private val objectMapper = ObjectMapper()

    val mockUser = User(
        UUID.randomUUID(),
        "mock",
        "User",
        "test@test.com",
        "test_password",
        Role.USER,
        UserPreferences()
    )

    @Test
    fun `should edit the user preferences`() {
        val request = UserEditRequest(null, "lastname", null,null)
        `when`(userService.updateUser(mockUser.username,
            arrayListOf(null,null,request.lastName, null)
        )).thenReturn("OK")
        val response = UserEditResponse("success")
        mockMvc.put("/api/v1/user/edit") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(response))
                }
            }
    }

    @Test
    fun `should delete the user by the user's initiative`() {
        val response = UserDeleteResponse(mockUser.username, true)
        `when`(userService.deleteUserByUsername(mockUser.username))
            .thenReturn(true)
        mockMvc.delete("/api/v1/user/delete") {
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }.andExpect {
            status { isOk() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(objectMapper.writeValueAsString(response))
            }
        }
    }
}