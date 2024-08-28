package com.hu.bme.aut.chess.backend.users.security.userDetails

import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private val repository: UserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: Optional<User> = repository!!.findUserByName(username)
        return if (user.isEmpty) throw UsernameNotFoundException("$username is an invalid username")

        else UserDetailsImpl(user.get())
    }
}
