package com.hu.bme.aut.chess.controller.user

import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import jakarta.persistence.*

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository) {

    fun findAll():MutableList<ChessUser> { return userRepository.findAll() }
    fun getUserByID(id: Long) { userRepository.findById(id) }
    fun save(user: ChessUser):ChessUser { return userRepository.save(user) }
}