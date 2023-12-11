package com.hwandefan.forum.api.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.hwandefan.forum.service.auth.AuthenticationService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class AuthenticationControllerTest {
    @Autowired
    private lateinit var mockMvc:MockMvc

    @MockBean
    private lateinit var authenticationService: AuthenticationService
    private val objectMapper = ObjectMapper()
    val authResponse = AuthenticationResponse("JWT_TOKEN_VALUE")


    @Test
    fun `should register the user`() {
        val registerRequest = RegisterRequest(
            "FirstName",
            "LastName",
            "test_email@test.com",
            "password"
        )
        `when`(authenticationService.register(registerRequest))
            .thenReturn(authResponse)
        mockMvc.post("/api/v1/auth/register") {
            content = objectMapper.writeValueAsString(registerRequest)
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(authResponse))
                }
            }
    }

    @Test
    fun `should authenticate the user by credentials`() {
        val authRequest = AuthenticationRequest(
            "test_email@test.com",
            "password"
        )
        `when`(authenticationService.authenticate(authRequest))
            .thenReturn(authResponse)
        mockMvc.post("/api/v1/auth/authentication") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(authRequest)
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(authResponse))
                }
            }
    }
}