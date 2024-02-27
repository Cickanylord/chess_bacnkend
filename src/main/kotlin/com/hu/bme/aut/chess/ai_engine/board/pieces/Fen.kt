package com.hu.bme.aut.chess.ai_engine.board.pieces

import com.hu.bme.aut.chess.Util.Quad
import com.hu.bme.aut.chess.ai_engine.board.BoardData
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Bishop
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece
import hu.bme.aut.android.monkeychess.board.pieces.*

import kotlin.IllegalArgumentException

data class Fen(val boardData: BoardData) {
    val activeColor: Char
        get() = boardData.activeColor.name[0].lowercaseChar()

    override fun toString(): String{
        //TODO en-passant + step counter
        return "${piecePlacement()} $activeColor ${castlingRights()} - 0 1"
    }

    fun piecePlacement(): String {
        var emptyTile = 0
        var boardFEN = ""


        boardData.board.forEach { row ->
            row.forEach { tile ->
                if(tile.piece != null) {
                    if(emptyTile > 0) {
                        boardFEN += emptyTile.toString()
                    }
                    emptyTile = 0

                    boardFEN += tile.piece?.letter
                } else {
                    emptyTile ++
                }

            }
            if(emptyTile > 0) {
                boardFEN += emptyTile.toString()
                emptyTile = 0
            }

            boardFEN += '/'
        }

        return boardFEN.dropLast(1)
    }

    fun castlingRights(): String {
        var castlingChars = ""
        if (boardData.castlingRights.t1) { castlingChars += 'K'}
        if (boardData.castlingRights.t2) { castlingChars += 'Q'}
        if (boardData.castlingRights.t3) { castlingChars += 'k'}
        if (boardData.castlingRights.t4) { castlingChars += 'q'}
        if (castlingChars.isBlank()) { return "-" }
        return castlingChars
    }
}


/*
fun buildBoardFromFen(fen: String): BoardData {
    val splitFen = fen.split(" ")
    val listOfPieces: MutableList<Piece> = piecePlacement(splitFen[0])
    val activeColor =
        if (splitFen[1] == "b") { PieceColor.BLACK }
        else if(splitFen[1] == "w") { PieceColor.WHITE }
        else { throw IllegalArgumentException("invalid char must be w/b actual: ${splitFen[1]}") }


    return BoardData(listOfPieces, activeColor, castlingRights(fen))
}

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

fun activeColor(activeColorChar: String): PieceColor {
    if (activeColorChar == "b") { return PieceColor.BLACK }
    else if(activeColorChar == "w") { return PieceColor.WHITE }
    else { throw IllegalArgumentException("invalid char must be w/b actual: $activeColorChar") }

}

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
in the Quad castling rights are stored
t1 = white king side
t2 = white queen side
t3 = black king side
t4 = black queen side


if castling is available then its set to true
 */
fun castlingRights(castlingChars: String): Quad {
    val rights = Quad(false, false, false, false)
    if(castlingChars == "-") { return rights }
    if (castlingChars.contains('K')) { rights.t1 = true }
    if (castlingChars.contains('Q')) { rights.t2 = true }
    if (castlingChars.contains('k')) { rights.t3 = true }
    if (castlingChars.contains('q')) { rights.t4 = true }


    return rights
}