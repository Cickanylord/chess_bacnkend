package com.hu.bme.aut.chess.ai_engine.board

import com.hu.bme.aut.chess.Util.Quad
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece
import kotlin.math.absoluteValue

fun main() {
    val boardData = BoardData("r3kbnr/pppppppp/8/8/3B4/8/8/R3K2R b Qq - 0 1")
    val board = BoardLogic(boardData)
    println(boardData.printBoard())
    cordinate()
    val king = board.board.getPiece(7,4)!!
    board.move(king, Pair(6,4))
    king.getValidSteps().forEach { println(it) }
    println("real")
    println(board.getLegalMoves(king))
    println(boardData.printBoard())

    //board.board.getPiece(5,2)!!.getValidSteps().forEach { println(it) }
    /*
    println(board.getAvailableSteps(board.board.getPiece(0,4)!!))
    board.board.getPiece(0,4)!!.getValidSteps().forEach { println(it) }
    board.step(board.board.getPiece(0,4)!!, Pair(0,3))
    println(Fen(boardData).toString())
    println(boardData.printBoard())

     */
}
class BoardLogic(val board: BoardData) {


    /**
     * This function gets all the legal steps for a piece
     * @param piece the piece which moves we get
     * @return the list of legal steps
     */
    fun getLegalMoves(piece: Piece): MutableList<Pair<Int, Int>> {
        val final: MutableList<Pair<Int, Int>> = mutableListOf()

        getLegalMovesInALine(piece, final)

        //castlingCheck

        return final
    }


    /**
     * Scans the pieces legal moves line by line
     * @param piece the piece which moves are getting calculated
     * @param final the list of the valid moves
     */
    fun getLegalMovesInALine(piece: Piece, final: MutableList<Pair<Int, Int>>) {
        piece.getValidSteps().forEach {
            for (i in it.indices) {
                val currentField = it[i]
                val currentPiece = board.getPiece(currentField.first, currentField.second)

                if (currentField != piece.position) {
                    //There is a piece on this field
                    if (currentPiece != null) {
                        //if the piece is from the other color it can be captured
                        if (piece.pieceColor != currentPiece.pieceColor) { final.add(currentField) }
                        //because pawns can only hit sideways -> it can't capture piece in front
                        if (currentField.second == piece.j && piece.name == PieceName.PAWN) { final.remove(currentField) }
                        //the piece can't be move past other pieces -> the lines has no more legal moves
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

    /**
     * Implement piece movement logic.
     * @param move the step we want to make
     * @param piece the piece we want to step with.
     *
     * The function invokes the get
     */

    fun move(piece: Piece, move: Pair<Int, Int>) {
        val moves = getLegalMoves(piece)
        if (moves.contains(move)) {
            //TODO EN-PASSANT AND CASTLING move

            //Moving the rook when castling
            if (piece.name == PieceName.KING) {
                val i = if(piece.pieceColor == PieceColor.WHITE) { 7 } else { 0 }

                //Castles KingSide rook
                if (move.second - piece.j == 2) {
                    board.movePiece(board.getPiece(i,7), Pair(i, 5))
                }
                //Castles QueenSide rook
                if (move.second - piece.j == -2) {
                    board.movePiece(board.getPiece(i,0), Pair(i, 3))
                }
            }
            //moves the piece on the board
            board.movePiece(piece, move)

            //updating the board after the step
            //setting castling rights after a step on the board
            //The king moves
            if (piece.name == PieceName.KING) {
                when (piece.pieceColor) {
                    PieceColor.WHITE -> { board.castlingRights = Quad(false, false, true, true) }
                    PieceColor.BLACK -> { board.castlingRights = Quad(true, true, false, false) }
                    else -> throw  IllegalArgumentException("no such color")
                }
            }
            //Rook moves
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
                PieceColor.WHITE -> { rights.t2.not() }
                PieceColor.BLACK -> { rights.t4.not() }
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
                PieceColor.WHITE -> { rights.t1.not() }
                PieceColor.BLACK -> { rights.t3.not() }
                else -> throw IllegalArgumentException("no such color")
            }
        }
        return false
    }
}