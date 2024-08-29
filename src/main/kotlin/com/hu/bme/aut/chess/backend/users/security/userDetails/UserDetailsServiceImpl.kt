package com.hu.bme.aut.chess.backend.users.security.userDetails

import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserRepository
import com.hu.bme.aut.chess.backend.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserDetailsServiceImpl(
    @Autowired
    private val userService: UserService
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return userService.findUserByName(username)
            ?.let { UserDetailsImpl(it) }
            ?: throw UsernameNotFoundException("$username is an invalid username")

    }
}
