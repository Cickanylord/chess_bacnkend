package com.hu.bme.aut.chess.ai_engine.board

import com.hu.bme.aut.chess.ai_engine.board.pieces.Fen
import com.hu.bme.aut.chess.ai_engine.board.pieces.buildBoardFromFen
import com.hu.bme.aut.chess.ai_engine.board.pieces.castlingRights
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Bishop
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece
import hu.bme.aut.android.monkeychess.board.TileNew
import hu.bme.aut.android.monkeychess.board.pieces.*


fun main() {
    val board = BoardData()

    buildBoardFromFen("rnbqk2r/pppp1ppp/4pn2/2b5/3P4/4PN2/PPP2PPP/RNBQKB1R w KQkq - 0 1").let {
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


open class BoardData() {
    var currentFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    var fen: Fen = Fen(this)

    var castlingRights = castlingRights(currentFen)

    var whiteSidePosition = Side.DOWN
    var BlackSidePosition = Side.UP
    var activeColor: PieceColor = PieceColor.WHITE
    var castlingAvailable = false//castlingRights.isFullOfFalse().not()

    val numRows = 8
    val numColumns = 8

    var board: Array<Array<TileNew>> = arrayOf(arrayOf())

    init {
        board = Array(numRows) { row ->
            Array(numColumns) { column ->

                //Black Pawn
                if (row == 1) { TileNew(false, Pawn(PieceColor.BLACK, row, column, Side.UP)) }
                //black Rooks
                else if ((row == 0 && column == 0) || (row == 0 && column == 7)) { TileNew(false, Rook(PieceColor.BLACK, row, column, Side.UP)) }

                //black Bishops
                else if ((row == 0 && column == 2) || (row == 0 && column == 5)) {  TileNew(false, Bishop(PieceColor.BLACK, row, column, Side.UP)) }

                //black Knights
                else if ((row == 0 && column == 1) || (row == 0 && column == 6)) {  TileNew(false, Knight(PieceColor.BLACK, row, column, Side.UP)) }
                //black Queen
                else if ((row == 0 && column == 3)) {  TileNew(false, Queen(PieceColor.BLACK, row, column, Side.UP)) }

                //black King
                else if ((row == 0 && column == 4)) {  TileNew(false, King(PieceColor.BLACK, row, column, Side.UP)) }

                //White Side
                else if (row == 6) {  TileNew(false, Pawn(PieceColor.WHITE, row, column, Side.DOWN)) }

                //White Rooks
                else if ((row == 7 && column == 0) || (row == 7 && column == 7)) {  TileNew(false, Rook(PieceColor.WHITE, row, column, Side.DOWN)) }

                //White Bishops
                else if ((row == 7 && column == 2) || (row == 7 && column == 5)) {  TileNew(false, Bishop(PieceColor.WHITE, row, column, Side.DOWN)) }

                //White Knights
                else if ((row == 7 && column == 1) || (row == 7 && column == 6)) {  TileNew(false, Knight(PieceColor.WHITE, row, column, Side.DOWN)) }

                //White Queen
                else if (row == 7 && column == 3) {  TileNew(false, Queen(PieceColor.WHITE, row, column, Side.DOWN)) }

                //White King
                else if (row == 7 && column == 4) {  TileNew(false, King(PieceColor.WHITE, row, column, Side.DOWN)) }

                //empty tiles
                else { TileNew(false, null) }
            }
        }

    }

    constructor(pieces: MutableList<Piece>, activeColor: PieceColor): this() {
        this.activeColor = activeColor
        Array(numRows) { row ->
            Array(numColumns) { column ->  TileNew(false, null)
            }
        }
        loadBoard(pieces)
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
                boardString += tile.piece?.letter ?: if(tile.free) {"-"} else {"x"}
            }
            boardString += "\n"
        }
        return boardString
    }

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