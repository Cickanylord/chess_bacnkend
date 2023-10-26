package com.hu.bme.aut.chess.repository.user

import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import jakarta.persistence.*
import java.util.*

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository) {

    fun findAll():MutableList<ChessUser>? { return userRepository.findAll() }
    fun getUserByID(id: Long): ChessUser? {
        val user = userRepository.findById(id)
        return if (!user.isEmpty) user.get()
        else null
    }
    fun save(user: ChessUser):ChessUser { return userRepository.save(user) }
}