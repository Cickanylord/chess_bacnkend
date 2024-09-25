package com.hu.bme.aut.chess.backend.security

import com.hu.bme.aut.chess.backend.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfig(
    private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint,
    private val authenticationProvider: AuthenticationProvider
) {

    @Bean
    fun filterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): SecurityFilterChain {
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
                auth.requestMatchers("/api/auth").permitAll()
                auth.requestMatchers("/api/user/me").permitAll()
                auth.requestMatchers("/api/user/addFriend").authenticated()
                auth.requestMatchers("/api/user/friends").authenticated()
                auth.requestMatchers("/api/chess/ai/fen").permitAll()
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
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
}
