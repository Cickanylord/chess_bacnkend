package com.hu.bme.aut.chess.controller.match

data class MatchRequest(
    val playerOne: Long,
    val playerTwo: Long,
    val board: String
)
