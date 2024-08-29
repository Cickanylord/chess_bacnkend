package com.hu.bme.aut.chess.backend.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(JWTProperties::class)
class JWTConfiguration