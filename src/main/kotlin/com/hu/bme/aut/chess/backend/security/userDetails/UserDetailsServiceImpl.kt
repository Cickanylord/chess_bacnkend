package com.hu.bme.aut.chess.backend.security.userDetails

import com.hu.bme.aut.chess.backend.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

typealias ApplicationUser = com.hu.bme.aut.chess.backend.users.User

@Service
class UserDetailsServiceImpl(
    @Autowired
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return (userRepository.findUserByName(username)
            ?.let {  UserDetailsImpl(it)  }
            ?: throw UsernameNotFoundException("$username is an invalid username"))
    }

    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.getName())
            .password(this.getPassword())
            .roles(this.getRoles().toString())
            .build()

}
