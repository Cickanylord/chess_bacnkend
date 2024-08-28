package com.hu.bme.aut.chess.backend.users

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

    fun saveUser(user: User): User {
        user.setPassword(passwordEncoder.encode(user.getPassword()))
        println(user.toString())
        return userRepository.save(user)
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

}