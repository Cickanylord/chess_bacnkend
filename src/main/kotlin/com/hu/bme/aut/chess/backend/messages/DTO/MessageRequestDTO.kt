package com.hu.bme.aut.chess.backend.messages.DTO

data class MessageRequestDTO(
    val sender_id: Long,
    val receiver_id:Long,
    val text: String
)
