package com.hu.bme.aut.chess.controller.message

data class MessageBody(
    val sender_id: Long,
    val receiver_id:Long,
    val text: String
)
