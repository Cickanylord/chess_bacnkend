package com.hu.bme.aut.chess.backend.match.DTO

data class MatchResponseDTO(
    val id: Long,
    val challenger: Long,
    val challenged: Long,
    val board: String
)
