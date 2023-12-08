package com.hwandefan.forum.service

import com.hwandefan.forum.api.auth.AuthenticationRequest
import com.hwandefan.forum.api.auth.RegisterRequest
import com.hwandefan.forum.config.jwt.JwtService
import com.hwandefan.forum.model.Role
import com.hwandefan.forum.model.User
import com.hwandefan.forum.model.UserPreferences
import com.hwandefan.forum.repository.user.UserRepository
import com.hwandefan.forum.service.auth.AuthenticationService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.security.core.Authentication
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class AuthenticationServiceTest {
    @InjectMocks
    private lateinit var authenticationService: AuthenticationService

    @Mock
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var authenticationManager: AuthenticationManager
    @Mock
    private lateinit var passwordEncoder:PasswordEncoder
    @Mock
    private lateinit var jwtService:JwtService

    private val regRequest = RegisterRequest(
        "Test Name",
        "Test LastName",
        "test@test.com",
        "123456789"
    )

    private val authRequest = AuthenticationRequest(
        "test@test.com",
        "123456789"
    )

    private val mockUser = User(
        UUID.randomUUID(),
        regRequest.firstname,
        regRequest.lastname,
        regRequest.email,
        regRequest.password,
        Role.USER,
        UserPreferences()
    )

    @Test
    fun `should register the user`() {
        `when`(passwordEncoder.encode(regRequest.password))
            .thenReturn("\$2a\$12\$/veaYAVNG7jG5K6.I8UhsOoruiwPN6GYiW/zhPccBKzloNpUsoHp2")
        authenticationService.register(regRequest)
        verify(userRepository).save(Mockito.any(User::class.java))
    }

    @Test
    fun `should authenticate the user`() {
        `when`(userRepository.findByEmail(authRequest.email))
            .thenReturn(mockUser)
        val response = authenticationService.authenticate(authRequest)
        verify(authenticationManager).authenticate(Mockito.any(Authentication::class.java))
    }
}