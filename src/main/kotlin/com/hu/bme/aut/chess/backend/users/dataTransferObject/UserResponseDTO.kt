package com.hu.bme.aut.chess.backend.users.dataTransferObject

import com.hu.bme.aut.chess.backend.users.UserRole


data class UserResponseDTO(
    val id: Long,
    val name: String,
    val roles: Set<UserRole>,
    val messagesSent: List<Long>,
    val messagesReceived: List<Long>,
    val challenger: List<Long>,
    val challenged: List<Long>,
    )