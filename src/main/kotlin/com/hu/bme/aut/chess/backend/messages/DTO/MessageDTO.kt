package com.hu.bme.aut.chess.backend.messages.DTO

import java.time.LocalDateTime

data class MessageDTO(
    val id: Long,
    val sender: Long,
    val receiver: Long,
    val text: String,
    val sentDate: LocalDateTime
    )
