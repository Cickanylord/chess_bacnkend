package com.hu.bme.aut.chess.backend.users.security.userDetails

import com.hu.bme.aut.chess.backend.users.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors


class UserDetailsImpl(private val user: User) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return user.getRoles().map { userRole -> SimpleGrantedAuthority(userRole.toString()) }
    }

    val id: Long
        get() = user.getId()!!

    override fun getPassword(): String {
        return user.getPassword()
    }

    override fun getUsername(): String {
        return user.getName()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

