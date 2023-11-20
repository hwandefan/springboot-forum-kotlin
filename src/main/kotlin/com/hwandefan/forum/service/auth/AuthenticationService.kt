package com.hwandefan.forum.service.auth

import com.hwandefan.forum.api.auth.AuthenticationRequest
import com.hwandefan.forum.api.auth.AuthenticationResponse
import com.hwandefan.forum.api.auth.RegisterRequest
import com.hwandefan.forum.config.jwt.JwtService
import com.hwandefan.forum.model.Role
import com.hwandefan.forum.model.User
import com.hwandefan.forum.repository.user.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
    ) {
    fun register(request: RegisterRequest):AuthenticationResponse {
        val user = User(
            UUID.randomUUID(),
            request.firstname,
            request.lastname,
            request.email,
            passwordEncoder.encode(request.password),
            Role.USER
        )
        userRepository.save(user)
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

    fun authenticate(request: AuthenticationRequest):AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )
        val user:UserDetails = userRepository.findByEmail(request.email)?:throw Exception("User Not Found")
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

}