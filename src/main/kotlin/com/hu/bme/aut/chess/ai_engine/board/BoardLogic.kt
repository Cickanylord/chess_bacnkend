package com.hu.bme.aut.chess.ai_engine.board

import com.hu.bme.aut.chess.ai_engine.board.pieces.buildBoardFromFen
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece

fun main() {
    val boardData: BoardData = buildBoardFromFen("rnbqkbnr/1ppppppp/8/8/p7/B7/2PPPPPP/RN1QKBNR b KQkq - 0 1")
    val board = BoardLogic(boardData)
    println(boardData.printBoard())
    cordinate()
    println(board.getAvailableSteps(board.board.getPiece(5,0)!!))
    board.board.getPiece(5,0)!!.getValidSteps().forEach { println(it) }
}
class BoardLogic(val board: BoardData){

    fun getAvailableSteps(piece: Piece): MutableList<Pair<Int, Int>> {
        val final: MutableList<Pair<Int, Int>> = mutableListOf()

        getAvailableStepsInaLine(piece, final)

        //castlingCheck
        if (board.castlingAvailable && piece.name == PieceName.KING) {

        }

        return final
    }


/**
Checks the available steps in a line and removes the invalids moves from the possible moves

 */
    fun getAvailableStepsInaLine(piece: Piece, final: MutableList<Pair<Int, Int>>) {
        piece.getValidSteps().forEach {
            for (i in it.indices) {
                val currentField = it[i]
                val currentPiece = board.getPiece(currentField.first, currentField.second)

                if (currentField != piece.position) {
                    //pawns

                    //other pieces
                    if (currentPiece != null) {
                        //if the piece is from the other color it can be removed
                        if (piece.pieceColor != currentPiece.pieceColor) { final.add(currentField) }
                        //because pawns can only hit sideways -> it can't remove piece in front
                        if (currentField.second == piece.j && piece.name == PieceName.PAWN) { final.remove(currentField) }
                        //the piece can't move forward if there is a piece in the way.
                        break
                    }
                    if (piece.name != PieceName.PAWN || currentField.second == piece.j) { final.add(currentField) }
                }
            }
        }
    }
}