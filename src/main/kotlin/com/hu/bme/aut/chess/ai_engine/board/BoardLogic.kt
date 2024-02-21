package com.hu.bme.aut.chess.ai_engine.board

import com.hu.bme.aut.chess.Util.Quad
import com.hu.bme.aut.chess.ai_engine.board.pieces.Fen
import com.hu.bme.aut.chess.ai_engine.board.pieces.buildBoardFromFen
import com.hu.bme.aut.chess.ai_engine.board.pieces.castlingRights
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece
import hu.bme.aut.android.monkeychess.board.TileNew
import hu.bme.aut.android.monkeychess.board.pieces.Queen
import kotlin.math.absoluteValue

fun main() {
    val boardData: BoardData = buildBoardFromFen("r3kbnr/pppppppp/8/8/3B4/2Q5/8/R3K1pR b KQkq - 0 1")
    val board = BoardLogic(boardData)
    println(boardData.printBoard())
    cordinate()
    //board.board.getPiece(5,2)!!.getValidSteps().forEach { println(it) }
    println(board.getAvailableSteps(board.board.getPiece(7,4)!!))
    board.board.getPiece(7,4)!!.getValidSteps().forEach { println(it) }
    board.step(board.board.getPiece(7,7)!!, Pair(6,7))
    println(Fen(boardData).toString())
    println(boardData.printBoard())
}
class BoardLogic(val board: BoardData) {

    fun getAvailableSteps(piece: Piece): MutableList<Pair<Int, Int>> {
        val final: MutableList<Pair<Int, Int>> = mutableListOf()

        getAvailableStepsInALine(piece, final)

        //castlingCheck

        return final
    }


/**
Checks the available steps in a line and removes the invalids moves from the possible moves
@Param piece
 */
    fun getAvailableStepsInALine(piece: Piece, final: MutableList<Pair<Int, Int>>) {
        piece.getValidSteps().forEach {
            for (i in it.indices) {
                val currentField = it[i]
                val currentPiece = board.getPiece(currentField.first, currentField.second)

                if (currentField != piece.position) {
                    //other pieces
                    if (currentPiece != null) {
                        //if the piece is from the other color it can be removed
                        if (piece.pieceColor != currentPiece.pieceColor) { final.add(currentField) }
                        //because pawns can only hit sideways -> it can't remove piece in front
                        if (currentField.second == piece.j && piece.name == PieceName.PAWN) { final.remove(currentField) }
                        break
                    }

                    if (piece.name != PieceName.PAWN || currentField.second == piece.j) { final.add(currentField) }

                    if (piece.name == PieceName.KING && (currentField.second - piece.j).absoluteValue == 2) {
                        //Invalid castling removing
                        //the king can't castle if it has no castling right or the king doesn't see the rook
                        if (piece.name == PieceName.KING) {
                            if (removeInvalidCastling(piece, currentField)) { final.remove(currentField) }
                        }
                    }
                }
            }
        }
    }

    fun step(piece: Piece, step: Pair<Int, Int>) {
        val steps = getAvailableSteps(piece)
        if (steps.contains(step)) {
            //TODO EN-PASSANT AND CASTLING STEPS
            board.movePiece(piece, step)
            if (piece.name == PieceName.KING && (step.second - piece.j).absoluteValue == 2) {

            }

            //updating the board after the step
            if (piece.name == PieceName.KING) {
                when (piece.pieceColor) {
                    PieceColor.WHITE -> { board.castlingRights = Quad(false, false, true, true) }
                    PieceColor.BLACK -> { board.castlingRights = Quad(true, true, false, false) }
                    else -> throw  IllegalArgumentException("no such color")
                }
            }
            if (piece.name == PieceName.ROOK) {
                when(piece.j) {
                    //QueenSide
                    0 -> {
                        if (piece.pieceColor == PieceColor.WHITE) { board.castlingRights[1] = false }
                        else { board.castlingRights[3] = false }
                    }
                    //KingSide
                    7 -> {
                        if (piece.pieceColor == PieceColor.WHITE) { board.castlingRights[0] = false }
                        else { board.castlingRights[2] = false }
                    }
                    else -> {}
                }
            }
        }
        //setting castling rights after a step on the board
        //The king moves

    }



    /**
     * Gives back true if the castling step is not available
     */

    fun removeInvalidCastling(piece: Piece, filedPos: Pair<Int, Int>): Boolean {
        val rights = board.castlingRights
        //QueenSide
        if (filedPos.second == piece.j - 2) {
            //checks if there is a piece between the king and rook
            for (k in 1..3) {
                //returns true if there is a piece between the king and rook
                board.getPiece(piece.i, piece.j - k)?.let {
                    return true 
                }

            }
            //checks for castling right
            return when (piece.pieceColor) {
                PieceColor.WHITE -> { rights.t1.not() }
                PieceColor.BLACK -> { rights.t3.not() }
                else -> throw IllegalArgumentException("no such color")
            }
        }
        //KingSideCastling
        if (filedPos.second == piece.j + 2) {
            //checks if there is a piece between the king and rook
            for (k in 1..2) {
                if (board.getPiece(piece.i, piece.j + k) != null) { return true }
            }
            //checks for castling rights
            return when(piece.pieceColor) {
                PieceColor.WHITE -> { rights.t2.not() }
                PieceColor.BLACK -> { rights.t4.not() }
                else -> throw IllegalArgumentException("no such color")
            }
        }
        return false
    }
}