package com.hu.bme.aut.chess.backend.security.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(
   prefix =  "jwt"
)
data class JWTProperties(
    val key: String = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437",
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long
)