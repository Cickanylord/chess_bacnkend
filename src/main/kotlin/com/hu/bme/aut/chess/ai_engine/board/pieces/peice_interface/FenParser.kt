package com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface


import com.hu.bme.aut.chess.Util.CastlingRights
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import hu.bme.aut.android.monkeychess.board.pieces.*

/**
 * This class provides functions for parsing FEN strings
 *
 *  @author Waldmann Tamás(EO229S)
 *
 *  for more information on fen annotation
 *  https://www.chess.com/terms/fen-chess
 *  To view and edit fen strings
 *  https://www.dailychess.com/chess/chess-fen-viewer.php
 */

object FenParser {
    /**
     * This function parses the first part of the Fen string which is represents the pieces on the board.
     *
     * @param piecePlacementString The string representing the piece placement on the board.
     * @return the list of pieces on the board.
     */
    fun piecePlacement(piecePlacementString: String): MutableList<Piece> {
        val listOfPieces: MutableList<Piece> = mutableListOf()
        var i: Int = 0
        var j: Int = 0
        piecePlacementString.forEach() { fenChar ->
            //empty tiles
            if(fenChar.isDigit()) {
                j += fenChar.toString().toInt() -1
            }
            //pieces
            else if(fenChar != '/') { listOfPieces.add(fenCharToPiece(fenChar, i, j)) }
            //row end
            j++
            //line end
            if (fenChar == '/') {
                i++
                j = 0
            }
        }
        return listOfPieces
    }

    /**
     * This function read from the Fen string active color char the active color on the board.
     *
     * @param activeColorChar the char representation of the active color
     * @return the color of the piece which is active ont he board.
     */

    fun activeColor(activeColorChar: String): PieceColor {
        return when (activeColorChar) {
            "b" -> {
                PieceColor.BLACK
            }
            "w" -> {
                PieceColor.WHITE
            }
            else -> { throw IllegalArgumentException("invalid char must be w/b actual: $activeColorChar") }
        }
    }

    /**
     * This function crates the corresponding piece from a fen char
     *
     * @param fenChar the char represeting the chess piece
     * @param i the index of column of the chess piece
     * @param j the index of row of the chess piece
     * @return the chees piece corresponding of the fen char
     * @throws IllegalArgumentException if the fenChar is invalid
     */
    fun fenCharToPiece(fenChar: Char, i: Int, j: Int): Piece {
        val side: Side
        val pieceColor = if(fenChar.isLowerCase()) {
            side = Side.UP
            PieceColor.BLACK
        }
        else {
            side = Side.DOWN
            PieceColor.WHITE
        }
        return when(fenChar) {
            'q','Q' -> Queen(pieceColor, i, j, side)
            'p','P' -> Pawn(pieceColor, i, j, side)
            'b','B' -> Bishop(pieceColor, i, j, side)
            'r','R' -> Rook(pieceColor, i, j, side)
            'n','N' -> Knight(pieceColor, i, j, side)
            'k','K' -> King(pieceColor, i, j, side)

            else -> {throw IllegalArgumentException("Invalid FEN character: $fenChar")}
        }
    }


    /**
     * this function parses the castling rights from castling rights part of the fen string
     *
     * @param castlingChars the string representation of the castling rights
     *
     * @return CastlingRight representing the castling rights. if the castling option is available then it contains true
     *
     * kw = white king side
     * qw = white queen side
     * kb = black king side
     * qb = black queen side
     *
     */
    fun castlingRights(castlingChars: String): CastlingRights {
        val rights = CastlingRights(false, false, false, false)
        if(castlingChars == "-") { return rights }
        if (castlingChars.contains('K')) { rights.kw = true }
        if (castlingChars.contains('Q')) { rights.qw = true }
        if (castlingChars.contains('k')) { rights.kb = true }
        if (castlingChars.contains('q')) { rights.qb = true }


        return rights
    }
}