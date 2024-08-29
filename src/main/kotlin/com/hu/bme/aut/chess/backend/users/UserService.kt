package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.users.DTO.UserRequestDTO
import com.hu.bme.aut.chess.backend.users.security.UserRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private var passwordEncoder: PasswordEncoder

) {
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).get()
    }

    fun saveUser(userRequestDTO: UserRequestDTO): User {
        val user = User()
        user.setPassword(passwordEncoder.encode(userRequestDTO.password))
        user.setName(userRequestDTO.name)

        println(user.toString())
        return userRepository.save(user)
    }

    fun grantAuthority(id: Long,role: UserRole): User? {
        getUserById(1)?.let {
            it.getRoles().add(role)
            return it
        } ?: return null
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

}