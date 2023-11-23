package com.hu.bme.aut.chess.controller.match

data class StepRequest(
    val match_id: Long,
    val prevBoard: String,
    val board: String,
)


