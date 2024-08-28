package com.hu.bme.aut.chess.backend.users.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfig(
    private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { }
            .csrf{ obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                auth.requestMatchers(HttpMethod.GET, "/api/user").authenticated()
                auth.requestMatchers("/api/user/{id}").authenticated()
                auth.requestMatchers("/api/messages").authenticated()
                auth.requestMatchers("/api/messages/{id}").authenticated()
                auth.requestMatchers("/api/chessMatch").permitAll()
                auth.requestMatchers("/api/chessMatch/*").permitAll()

            }
            .logout { logout -> logout.logoutSuccessHandler(LogoutSuccessHandler()) }
            .httpBasic { auth -> auth.authenticationEntryPoint(restAuthenticationEntryPoint) }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER) }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowCredentials = true
        configuration.allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
