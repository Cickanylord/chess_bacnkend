/*
package com.hu.bme.aut.chess.config


import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke{
            authorizeRequests {
                authorize(anyRequest, permitAll)
                authorize(anyRequest, permitAll)

            }
            csrf {
                ignoringRequestMatchers("/h2-console/**")

            }

            formLogin { }
            logout {  }
        }

        return http.build()
    }

    @Bean
    fun users(): UserDetailsService {
        val user = User.builder()
            .username("user")
            .password("geci")
            .roles("USER")
            .build()
        val admin = User.builder()
            .username("admin")
            .password("geci")
            .roles("USER", "ADMIN")
            .build()
        return InMemoryUserDetailsManager(user, admin)
    }
}


 */



 */