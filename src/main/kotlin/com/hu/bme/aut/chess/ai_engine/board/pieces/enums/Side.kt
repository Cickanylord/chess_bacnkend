package com.hu.bme.aut.chess.ai_engine.board.pieces.enums

enum class Side {
    UP, DOWN;

    fun oppositeSide(): Side {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
        }
    }
}