package com.hu.bme.aut.chess.ai_engine.board

import com.hu.bme.aut.chess.Util.Quad
import com.hu.bme.aut.chess.ai_engine.board.pieces.*
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Bishop
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece
import hu.bme.aut.android.monkeychess.board.TileNew
import hu.bme.aut.android.monkeychess.board.pieces.*


fun main() {
//val board = BoardData()

    BoardData("rnbqk2r/pppp1ppp/4pn2/2b5/3P4/4PN2/PPP2PPP/RNBQKB1R w KQkq - 0 1").let {
        println(it.fen)


        /*
        println(it.printBoard())
        println()
        it.flipTheTable()
        println(it.printBoard())
        println(it.fen)

         */

    }
    //board.cleanTheBoard()
    //println(Fen().createFEN(board))
    //println(board.printBoard())
    //Fen(board).let { println(it.printFen()) }
}


class BoardData(fenString: String) {
    var fen: Fen
    var castlingRights: Quad = Quad(true, true, true, true)
        set(rights: Quad) {
            for (i in 0..3) {
                if (!castlingRights[i]) {
                    rights[i] = false
                }
            }
            field = rights
        }

    var whiteSidePosition = Side.DOWN
    var BlackSidePosition = Side.UP
    var activeColor: PieceColor
    var castlingAvailable: Boolean //castlingRights.isFullOfFalse().not()

    val numRows = 8
    val numColumns = 8

    val whiteHasCastlingRight
        get() = (castlingRights.t1 || castlingRights.t2)
    val blackHasCastlingRight
        get() = (castlingRights.t3 || castlingRights.t4)

    var board: Array<Array<TileNew>> = arrayOf(arrayOf())

    init {
        val splitFen = fenString.split(" ")

        board = Array(numRows) { row ->
            Array(numColumns) { column ->
                TileNew(false, null)
            }
        }

        loadBoard(piecePlacement(splitFen[0]))
        activeColor = activeColor(splitFen[1])
        castlingRights = castlingRights(splitFen[2])
        castlingAvailable = castlingRights.hasTrue()
        fen = Fen(this)
    }

    fun cleanTheBoard() {
        getAllPieces().forEach() { removePiece(it) }
    }

    fun getPiecesByColor(color: PieceColor): List<Piece> {
        return getAllPieces().filter { it.pieceColor == color }
    }

    fun getAllPieces(): MutableList<Piece> {
        val listOfPieces = mutableListOf<Piece>()
        board.forEach {
            it.forEach {
                it.piece?.let { listOfPieces.add(it) }
            }
        }
        return listOfPieces
    }


    fun getPiece(row: Int, col: Int): Piece? {
        return board[row][col].piece
    }

    fun getPiece(pos: Pair<Int, Int>): Piece {
        val piece = board[pos.first][pos.second].piece
        return piece!!
    }
    fun movePiece(piece: Piece?, pos: Pair<Int, Int>) {
        //add piece
        board[pos.first][pos.second].piece = piece
        piece?.let {
            //remove piece
            board[piece.i][piece.j].piece = null

            //changes the piece position
            it.step(pos.first, pos.second)
        }
    }

    /**
     * after the pieces are loaded in this function adds the pieces to their place on the board.
     */
    fun addPiece(piece: Piece) {
        board[piece.i][piece.j] = TileNew(false, piece)
    }
    fun removePiece(i: Int, j: Int) {
        board[i][j] = TileNew(false, null)
    }

    fun removePiece(piece: Piece) {
        board[piece.i][piece.j] = TileNew(false, null)

    }

    fun loadBoard(pieces: List<Piece>){
        cleanTheBoard()
        pieces.forEach(){
            addPiece(it)
        }
    }

    fun flipTheTable() {
        whiteSidePosition = whiteSidePosition.oppositeSide()
        BlackSidePosition = BlackSidePosition.oppositeSide()

        getAllPieces().forEach() { it.flip() }
        loadBoard(getAllPieces())
    }

    /*
        Debug
    */


    fun printBoard(): String {
        var boardString = ""

        board.forEach {
            it.forEach() { tile ->
                boardString += tile.piece?.letter ?: if(!tile.free) {"-"} else {"X"}
            }
            boardString += "\n"
        }
        return boardString
    }

    fun colorHasCastlingRight(color: PieceColor): Boolean {
        return when(color) {
            PieceColor.WHITE -> {
                (castlingRights.t1 || castlingRights.t2)
            }

            PieceColor.BLACK -> {
                (castlingRights.t3 || castlingRights.t4)
            }

            else -> throw IllegalArgumentException("there is no such color $color")
        }
    }

    /**
     * if there is a true value in the castling rights quad it sets it to true or false
     * if the right is false it can't be set to true again.
     */

}


fun cordinate(){
    var string = ""
    for(i in 0..7) {
        for (j in 0..7) {
            string += "(i: $i,j: $j)"
        }
        string += "\n"
    }
    println(string)
}