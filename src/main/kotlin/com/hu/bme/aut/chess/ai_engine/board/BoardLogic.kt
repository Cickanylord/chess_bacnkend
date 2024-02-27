package com.hu.bme.aut.chess.ai_engine.board

import com.hu.bme.aut.chess.Util.Quad
import com.hu.bme.aut.chess.ai_engine.board.pieces.castlingRights
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece
import kotlin.math.absoluteValue

fun main() {
    //TODO
    //there is something wrong with castling. if the rook is removed by piece the castling right is not revoked
    //The castling right isn't revoked when the rook is captured
    //debug this
    //Bn2kbnr/p1p1pppp/8/3N4/2q3P1/5P2/PPPPP2P/R1BQK2R w KQkq - 0 1
    //rn2kbnr/pBp1pppp/4b3/4q3/6P1/2N5/PPPPPP1P/R1BQK2R w KQkq - 0 1
    //Bn2kbnr/p1p1pppp/8/3N4/2q3P1/5P2/PPPPP2P/R1BQK2R w KQkq - 0 1

    //new bug castling right? 
    //rn2kbnr/pBp1pppp/4b3/4q3/6P1/2N5/PPPPPP1P/R1BQK2R w KQkq - 0 1



    val boardData = BoardData("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    val board = BoardLogic(boardData)
   // println(boardData.fen)
    //println(boardData.printBoard())
    cordinate()
    //val king = board.board.getPiece(0,2)!!
   // println(board.getLegalMoves(king))






    var color = PieceColor.WHITE

    while (true) {
        val ai = NewAI(color ,boardData)
        val nextStep = ai.getTheNextStep()
        board.move(nextStep.first, nextStep.second)

        println(boardData.fen.toString())
        println(boardData.printBoard())
        color = color.oppositeColor()
    }






   // king.getAllMoves().forEach { println(it) }


    //println(board.getLegalMoves(king))
    //println(boardData.printBoard())

    //board.board.getPiece(5,2)!!.getAllMoves().forEach { println(it) }
    /*
    println(board.getAvailableSteps(board.board.getPiece(0,4)!!))
    board.board.getPiece(0,4)!!.getAllMoves().forEach { println(it) }
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
    fun getLegalMoves(piece: Piece): MutableList<Pair<Int, Int>> {
        val final: MutableList<Pair<Int, Int>> = mutableListOf()

        getPieceVision(piece, final)
        //removeMovesResultingInCheck(piece, final)



        return  final

            .filter() { pos ->

                //init new board to check if the king is in check.
                val tmpBoard = BoardLogic(BoardData(board.fen.toString()))
                tmpBoard.board.movePiece(tmpBoard.board.getPiece(piece.position),pos)

                //if the king in check it can't castle.
                if(piece.name == PieceName.KING) {
                    if (scanBoardForCheck(piece.pieceColor) && (pos.second - piece.j).absoluteValue == 2) {
                        //filters out castling moves
                        return@filter false
                    }
                }


                //scans if the king is in check after movement
                !scanBoardForCheck(piece.pieceColor, tmpBoard)
        } as MutableList<Pair<Int, Int>>




    }


    /**
     * Scans the pieces vision line by line puts the truly visible fields in a list
     * @param piece the piece which moves are getting calculated
     * @param final the list of the visible fields
     */
    fun getPieceVision(piece: Piece, final: MutableList<Pair<Int, Int>>) {
        piece.getAllMoves().forEach {
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



                    if (piece.name == PieceName.KING && (currentField.second - piece.j).absoluteValue == 2 ) {
                        //Invalid castling removing
                        //the king can't castle if it has no castling right or the king doesn't see the rook
                        if (board.colorHasCastlingRight(piece.pieceColor)) {
                            if (removeInvalidCastling(piece, currentField)) { final.remove(currentField) }
                        } else {
                            final.remove(currentField)
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
            //TODO EN-PASSANT

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

            //Check if the corners have the original rook
            val corners = board.scanCorners()
            for (i in 0..3) {
                when (i) {
                    0 -> {
                        if(!corners[i]) {
                            board.castlingRights[0] = false
                        }
                    }
                    1 -> {
                        if(!corners[i]) {
                            board.castlingRights[1] = false
                        }
                    }
                    2 -> {
                        if(!corners[i]) {
                            board.castlingRights[2] = false
                        }
                    }
                    3 -> {
                        if(!corners[i]) {
                            board.castlingRights[3] = false
                        }
                    }
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
     * @param fieldPos the position where we want to castle
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
     * The king can't be too checked after a move because of the rules of chess
     *
     * @param piece the piece which movement could result in check for its king
     * @param pos the position where we want to move the piece
     *
     * @return if the king gets in check returns true
     */
    fun scanBoardForCheck(color: PieceColor, boardLogic: BoardLogic = this): Boolean {
        val listOfMovesOppositeColorCanDo: MutableList<Pair<Int, Int>> = mutableListOf()
        val king = boardLogic.board.getKing(color)
        boardLogic.board.getPiecesByColor(color.oppositeColor())
            .forEach() {
                boardLogic.getPieceVision(it, listOfMovesOppositeColorCanDo)
            }
         return listOfMovesOppositeColorCanDo.contains(king.position)
    }

    /**
     * This function removes the moves from the legal which would result in check
     *
     * @param piece the piece which movement could result in check for its king
     * @param final the possible legal moves the piece could make
     */
    fun removeMovesResultingInCheck(piece: Piece, final: MutableList<Pair<Int, Int>>) {
        val shouldRemove: MutableList<Pair<Int, Int>> = mutableListOf()
        final.forEach() { pos->
            val tmpBoard = BoardLogic(BoardData(board.fen.toString()))
            tmpBoard.board.movePiece(tmpBoard.board.getPiece(piece.position), pos)
            if (scanBoardForCheck(piece.pieceColor, tmpBoard)) {
                shouldRemove.add(pos)
            }
        }
        final.removeAll(shouldRemove)
    }

    fun getLegalStepsForColor(color: PieceColor):  MutableList<Pair<Int, Int>> {
        val final: MutableList<Pair<Int, Int>> = mutableListOf()
        board.getAllPieces().forEach() { final.addAll(getLegalMoves(it)) }
        return final
    }

    /**
     * This function gets all the piece with its moves
     *
     */
    fun getMovesWithPiece(color: PieceColor): List<Pair<Piece, Pair<Int,Int>>> {
        val steps = mutableListOf<Pair<Piece, Pair<Int,Int>>>()
        board.getPiecesByColor(color)
            .forEach() { piece->
            getLegalMoves(piece)
                .forEach(){ move->
                steps.add(Pair(piece,move))
            }
        }
        return steps
    }


}