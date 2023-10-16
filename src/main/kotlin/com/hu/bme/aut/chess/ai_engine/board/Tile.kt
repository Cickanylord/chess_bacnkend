package hu.bme.aut.android.monkeychess.board

import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece


data class Tile(var free: Boolean, var pice: Piece)