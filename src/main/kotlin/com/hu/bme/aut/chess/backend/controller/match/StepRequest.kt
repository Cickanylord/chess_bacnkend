package com.hu.bme.aut.chess.backend.controller.match

data class StepRequest(
    val match_id: Long,
    val prevBoard: String,
    val board: String,
)


