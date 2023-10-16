package com.hu.bme.aut.chess.ai_engine.board.pieces.enums

enum class PieceColor {
    BLACK, WHITE, EMPTY;

    fun oppositeColor(): PieceColor {
        return when (this) {
            BLACK -> WHITE
            WHITE -> BLACK
            EMPTY -> EMPTY
        }
    }
}


