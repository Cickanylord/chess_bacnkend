package com.hu.bme.aut.chess.backend.match.dataTransferObject

data class StepRequest(
    val match_id: Long,
    val prevBoard: String,
    val board: String,
)


