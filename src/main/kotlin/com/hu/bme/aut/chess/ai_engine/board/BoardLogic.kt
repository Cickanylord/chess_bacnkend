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
     * This function gets all the legal moves for a piece
     * @param piece the piece which moves we get
     * @return the list of legal moves
     */
    fun getLegalMoves(piece: Piece, scanForCheck: Boolean = true): MutableList<Pair<Int, Int>> {
        val final: MutableList<Pair<Int, Int>> = mutableListOf()

        getLegalMovesInALine(piece, final)

        if (scanForCheck) {
            final.filter { !scanForCheckForMovement(piece, it) }
        }
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
     * @param move the move we want to make
     * @param piece the piece we want to move with.
     *
     * The function invokes the getLegalMoves
     * if @param move is not in the list of legal moves then nothing happens
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

            //updating the board after the piece movement
            //setting castling rights after a movement on the board
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
     * This function checks if the king can legally do the castling move
     * @param piece the king which moves we check if ti can castle
     * @param filed the position where we want to castle
     * @return true if the castling move is not legal and return false if the move is legal
     */

    fun removeInvalidCastling(piece: Piece, fieldPos: Pair<Int, Int>): Boolean {
        val rights = board.castlingRights
        //QueenSide
        if (fieldPos.second == piece.j - 2) {
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
        if (fieldPos.second == piece.j + 2) {
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

    /**
     * This function scans if the next move would result in check for the current players king
     * The king can't be to checked after a move because of the rules of chess
     *
     * @param piece the piece which movement could result in chess for its king
     * @param pos the position where we want to move the piece
     *
     * @return if the king gets in check returns true
     */
    fun scanForCheckForMovement(piece: Piece, pos: Pair<Int, Int>): Boolean {
        val king = board.getKing(piece.pieceColor)
        BoardLogic(BoardData(board.fen.toString())).let { boardLogic ->
            boardLogic.move(boardLogic.board.getPiece(piece.position), pos)
            boardLogic.board.getPiecesByColor(piece.pieceColor.oppositeColor())
                .forEach { piece ->
                    if (getLegalMoves(piece, false).contains(king.position)) {return true}
            }
        }
        return false
    }

}