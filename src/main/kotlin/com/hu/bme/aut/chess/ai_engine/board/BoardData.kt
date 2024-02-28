package com.hu.bme.aut.chess.ai_engine.board


import com.hu.bme.aut.chess.Util.CastlingRights
import com.hu.bme.aut.chess.ai_engine.board.pieces.*
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.FenParser.activeColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.FenParser.castlingRights
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.FenParser.piecePlacement
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


data class BoardData(val fenString: String) {
    var fen: Fen
    var castlingRights: CastlingRights = CastlingRights(true, true, true, true)
        set(rights: CastlingRights) {
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
        get() = (castlingRights.kw || castlingRights.qw)
    val blackHasCastlingRight
        get() = (castlingRights.kw || castlingRights.qw)

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

            //if rook is captured or moved revoke castling right
            revokeCastlingRightIfRookAbsent(scanCorners())
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
        //scan the corners if whether the rook is absent
        revokeCastlingRightIfRookAbsent(scanCorners())
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
                (castlingRights.kw || castlingRights.qw)
            }

            PieceColor.BLACK -> {
                (castlingRights.kb || castlingRights.qb)
            }

            else -> throw IllegalArgumentException("there is no such color $color")
        }
    }

    /**
     * if there is a true value in the castling rights quad it sets it to true or false
     * if the right is false it can't be set to true again.
     */

    /**
     * This function gets the given colors king
     * @param color the color which kings we search for
     */
    fun getKing(color: PieceColor): Piece {
        for (i in 0..7) {
            for (j in 0..7)
                getPiece(i, j)?.let { kingCandidate->
                    if (kingCandidate is King && kingCandidate.pieceColor == color ) {
                        return kingCandidate
                    }
                }
        }
        throw IllegalArgumentException("There must be a king on the board")
    }

    /**
     * This function checks all the corners if there is the right rook on it
     * this is used for checking if there is a rook in the corner
     *
     * kw = white king side
     * qw = white queen side
     * kb = black king side
     * qb = black queen side
     *
     *
     * @return returns a list containing information about the corner the firs two are withe corners and the last two are black corners
     */
    fun scanCorners(): CastlingRights{
        val listOfCorners = listOf(getPiece(7, 7), getPiece(7, 0), getPiece(0, 7), getPiece(0, 0))
        return CastlingRights(
            //white king side rook
            kw = listOfCorners[0]?.name == PieceName.ROOK && listOfCorners[0]?.pieceColor == PieceColor.WHITE,
            //white queen side rook
            qw = listOfCorners[1]?.name == PieceName.ROOK && listOfCorners[1]?.pieceColor == PieceColor.WHITE,
            //black king side rook
            kb = listOfCorners[2]?.name == PieceName.ROOK && listOfCorners[2]?.pieceColor == PieceColor.BLACK,
            //black queen side rook
            qb = listOfCorners[3]?.name == PieceName.ROOK && listOfCorners[3]?.pieceColor == PieceColor.BLACK
        )
    }

    fun revokeCastlingRightIfRookAbsent(corners: CastlingRights) {
        for (i in 0..3) {
            if (!corners[i]) {
                castlingRights[i] = false
            }
        }
    }
}

fun comparePieces(piece1: Piece?, piece2: Piece?): Boolean {
    if (piece1 == null || piece2 == null) {return false}

    return piece1.name == piece2.name && piece1.pieceColor == piece2.pieceColor
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