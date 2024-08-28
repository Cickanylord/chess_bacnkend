package com.hu.bme.aut.chess.backend.users.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException


@Component
class RestAuthenticationEntryPoint : BasicAuthenticationEntryPoint() {
    private val objectMapper = Jackson2ObjectMapperBuilder.json().build<ObjectMapper>()

    @Throws(IOException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authEx: AuthenticationException) {
        val authHeader = String.format("Auth realm=\"%s\"", realmName)
        response.addHeader("WWW-Authenticate", authHeader)
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        val json = objectMapper.writeValueAsString("{\"error\":\"unauthorized\"}")
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(json)
    }

    override fun afterPropertiesSet() {
        realmName = "hu.bme.aut"
        super.afterPropertiesSet()
    }
}