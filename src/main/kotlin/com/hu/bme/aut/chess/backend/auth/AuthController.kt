package com.hu.bme.aut.chess.backend.auth

import com.hu.bme.aut.chess.backend.auth.dataTransferObject.AuthenticationRequestDTO
import com.hu.bme.aut.chess.backend.auth.dataTransferObject.AuthenticationResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api/auth")
class AuthController (
    private val authenticationService: AuthenticationService
){

    @PostMapping
    fun authenticate(@RequestBody authRequest: AuthenticationRequestDTO): ResponseEntity<AuthenticationResponseDTO> {
        return ResponseEntity.ok(authenticationService.authentication(authRequest))
    }

    /*
    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefereshAccessTokenRequest): ResponseEntity<AuthenticationResponseDTO> {
        authenticationService.refreshAccessToken(request.token)
            ?.mapToTokenResponse()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, )
        return ResponseEntity.ok(authenticationService.authentication(authRequest))
    }

     */

}