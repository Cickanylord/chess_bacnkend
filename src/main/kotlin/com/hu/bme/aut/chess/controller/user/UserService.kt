package com.hu.bme.aut.chess.controller.user

import com.hu.bme.aut.chess.domain.User
import com.hu.bme.aut.chess.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository) {

    fun findAll():MutableList<User> { return userRepository.findAll() }
    fun getUserByID(id: Long) { userRepository.findById(id) }
    fun save(user: User):User { return userRepository.save(user) }
}