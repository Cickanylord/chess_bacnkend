package com.hu.bme.aut.chess.backend.users.DTO

import com.hu.bme.aut.chess.backend.users.security.UserRole


data class UserResponseDTO(
    val _id: Long,
    val name: String,
    val roles: Set<UserRole>,
    val messagesSent: List<Long>,
    val messagesReceived: List<Long>,
    val challenger: List<Long>,
    val challenged: List<Long>,
    )