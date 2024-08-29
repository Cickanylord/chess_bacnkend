package com.hu.bme.aut.chess.backend.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler
import java.io.IOException


class LogoutSuccessHandler : SimpleUrlLogoutSuccessHandler() {
    private val objectMapper = Jackson2ObjectMapperBuilder.json().build<ObjectMapper>()

    @Throws(IOException::class, ServletException::class)
    override fun onLogoutSuccess(
        request: HttpServletRequest?, response: HttpServletResponse,
        authentication: Authentication?
    ) {
        val json = objectMapper.writeValueAsString("{\"message\":\"Successfully logged out\"}")
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(json)
    }
}