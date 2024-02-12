package hu.bme.aut.android.monkeychess.board

import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece


data class TileNew(var free: Boolean, var piece: Piece?)