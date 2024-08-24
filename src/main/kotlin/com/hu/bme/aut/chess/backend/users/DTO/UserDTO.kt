package com.hu.bme.aut.chess.backend.users.DTO


data class UserDTO(
    val _id: Long,
    val name: String,
    val messagesSent: List<Long>,
    val messagesReceived: List<Long>,

    )