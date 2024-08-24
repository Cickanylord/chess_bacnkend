package com.hu.bme.aut.chess.backend.games.chess.match

data class StepRequest(
    val match_id: Long,
    val prevBoard: String,
    val board: String,
)


