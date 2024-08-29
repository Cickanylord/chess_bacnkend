package com.hu.bme.aut.chess.backend.match.dataTransferObject

data class MatchRequestDTO(
    val challenger: Long,
    val challenged: Long,
    val board: String
)
