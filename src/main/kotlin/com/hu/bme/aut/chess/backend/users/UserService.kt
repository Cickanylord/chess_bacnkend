package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.security.userDetails.UserDetailsImpl
import com.hu.bme.aut.chess.backend.security.userDetails.UserDetailsServiceImpl
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserRequestDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private var passwordEncoder: PasswordEncoder,
) {
    fun findAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun findUserById(id: Long): User? {
        return userRepository.findById(id).get()
    }

    fun findUserByName(name: String): User? {
        return userRepository.findUserByName(name)
    }

    fun findAuthenticatedUser(): User?{
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.principal is UserDetails) {
            val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl
            return findUserById(userDetails.id)
        }
        return null
    }


    fun saveUser(userRequestDTO: UserRequestDTO): User {
        val user = User()
        user.setPassword(passwordEncoder.encode(userRequestDTO.password))

        user.setName(userRequestDTO.name)

        return userRepository.save(user)
    }

    fun grantAuthority(id: Long,role: UserRole): User? {
        findUserById(1)?.let {
            it.getRoles().add(role)
            return it
        } ?: return null
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}