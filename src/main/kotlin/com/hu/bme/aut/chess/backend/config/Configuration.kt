package com.hu.bme.aut.chess.backend.config

import com.hu.bme.aut.chess.backend.security.jwt.JWTProperties
import com.hu.bme.aut.chess.backend.users.UserRepository
import com.hu.bme.aut.chess.backend.security.userDetails.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JWTProperties::class)
class JWTConfiguration() {

    @Autowired
    private val userDetailsServiceImpl: UserDetailsServiceImpl? = null
    /*
    @Bean
    fun userDetailService(userRepository: UserRepository): UserDetailsService {
        return UserDetailsServiceImpl(userRepository)
    }

     */

    @Bean
    fun authenticationProvider(userRepository: UserRepository): AuthenticationProvider {
        return DaoAuthenticationProvider()
            .also {
                it.setUserDetailsService(userDetailsServiceImpl)
                it.setPasswordEncoder(encoder())
            }
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}