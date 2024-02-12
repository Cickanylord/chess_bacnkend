package com.hu.bme.aut.chess.ai_engine.board.pieces

import com.hu.bme.aut.chess.Util.Quad
import com.hu.bme.aut.chess.ai_engine.board.BoardData
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Bishop
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece
import hu.bme.aut.android.monkeychess.board.pieces.*

import kotlin.IllegalArgumentException

class Fen(val boardData: BoardData) {
    override fun toString(): String{
        return "${piecePlacement()} ${activeColor()}"
    }

    fun piecePlacement(): String {
        var emptyTile = 0
        var boardFEN = ""

        if (boardData.whiteSidePosition == Side.UP){
            boardData.flipTheTable()
        }

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

    fun activeColor(): Char{
        return boardData.activeColor.name[0].lowercaseChar()
    }
}

fun buildBoardFromFen(fen: String): BoardData {
    val splitFen = fen.split(" ")
    val listOfPieces: MutableList<Piece> = piecePlacement(splitFen[0])
    val activeColor =
        if (splitFen[1] == "b") { PieceColor.BLACK }
        else if(splitFen[1] == "w") { PieceColor.WHITE }
        else {throw IllegalArgumentException("invalid char must be w/b actual: ${splitFen[1]}")}


    return BoardData(listOfPieces, activeColor)
}

fun piecePlacement(fenFragment: String): MutableList<Piece>{
    val listOfPieces: MutableList<Piece> = mutableListOf()
    var i: Int = 0
    var j: Int = 0
    fenFragment.forEach() { fenChar ->
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


/*
in the Quad castling rights are stored
t1 = white queen side
t2 = white king side
t3 = white queen side
t4 = white king side

if castling is available then its set to true
 */
fun castlingRights(fen: String): Quad<Boolean,Boolean,Boolean,Boolean> {
    val rights = Quad(false, false, false, false)
    fen.split(" ")[1].let { castlingChars ->
        if(castlingChars == "-") { return rights }
        if (castlingChars.contains('K')) { rights.t1 = true }
        if (castlingChars.contains('Q')) { rights.t1 = true }
        if (castlingChars.contains('k')) { rights.t1 = true }
        if (castlingChars.contains('q')) { rights.t1 = true }
    }
    return rights
}