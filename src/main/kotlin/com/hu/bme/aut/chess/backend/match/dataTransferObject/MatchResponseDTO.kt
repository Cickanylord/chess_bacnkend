package com.hu.bme.aut.chess.backend.match.dataTransferObject

data class MatchResponseDTO(
    val id: Long,
    val challenger: Long,
    val challenged: Long,
    val board: String,
    val isGoing: Boolean,
    val winner: Long?,
    val loser: Long?,
)
