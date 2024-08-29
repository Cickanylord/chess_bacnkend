package com.hu.bme.aut.chess.backend.auth

import com.hu.bme.aut.chess.backend.auth.dataTransferObject.AuthenticationRequestDTO
import com.hu.bme.aut.chess.backend.auth.dataTransferObject.AuthenticationResponseDTO
import com.hu.bme.aut.chess.backend.security.jwt.JWTProperties
import com.hu.bme.aut.chess.backend.security.userDetails.UserDetailsServiceImpl
import com.hu.bme.aut.chess.backend.security.jwt.TokenService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: UserDetailsServiceImpl,
    private val tokenService: TokenService,
    private val jwtProperties: JWTProperties
) {
    fun authentication(authRequest: AuthenticationRequestDTO): AuthenticationResponseDTO {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.userName,
                authRequest.password
            )

        )

        val user = userDetailsService.loadUserByUsername(authRequest.userName)
        val accessToken = tokenService.generate(
            user,
            Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
        )
        return  AuthenticationResponseDTO(
            accessToken
        )
    }

}
