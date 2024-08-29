package com.hu.bme.aut.chess.ai_engine.board


import com.hu.bme.aut.chess.Util.CastlingRights
import com.hu.bme.aut.chess.ai_engine.board.FEN.Fen
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.FEN.FenParser.activeColor
import com.hu.bme.aut.chess.ai_engine.board.FEN.FenParser.castlingRights
import com.hu.bme.aut.chess.ai_engine.board.FEN.FenParser.piecePlacement
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece
import hu.bme.aut.android.monkeychess.board.TileNew
import hu.bme.aut.android.monkeychess.board.pieces.*
import kotlin.math.absoluteValue


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


/**
 * this class represents the state of the board and provides methods to manipulate the board
 *
 * @param fenString the fen string representation of the board
 * @author Waldmann Tamás(EO229S)
 */

data class BoardData(val fenString: String) {
    /** the fen generator for the Board*/
    var fen: Fen

    var possibleEnPassantTargets = "-"

    /** stores the castling rights. After initialization the castling right cannot be given back(can't set it true) */
    var castlingRights: CastlingRights = CastlingRights(true, true, true, true)
        set(rights: CastlingRights) {
            for (i in 0..3) {
                if (!castlingRights[i]) {
                    rights[i] = false
                }
            }
            field = rights
        }

    /** the current player which can move */
    var activeColor: PieceColor
    /** shows if castling is available on the board */
    var castlingAvailable: Boolean

    /** the number of row and columns on the chess board */
    val numRows = 8
    val numColumns = 8

    /** The board which stores the game data in a 2D array */
    var board: Array<Array<TileNew>> = arrayOf(arrayOf())

    /** This function initialize the board from the fen string given*/
    init {
        val splitFen = fenString.split(" ")

        // Creating the board
        board = Array(numRows) { row ->
            Array(numColumns) { column ->
                TileNew(false, null)
            }
        }

        //loading the data from fen to the board
        loadBoard(piecePlacement(splitFen[0]))
        activeColor = activeColor(splitFen[1])
        castlingRights = castlingRights(splitFen[2])
        castlingAvailable = castlingRights.hasTrue()
        possibleEnPassantTargets = splitFen[3]
        fen = Fen(this)
    }

    /**
     * This function removes all the pieces from the board
     */

    fun cleanTheBoard() {
        getAllPieces().forEach { removePiece(it) }
    }

    /**
     * This function returns a specific colors pieces
     * @param color the color which pieces we want to get.
     * @return a list of pieces from a specific color.
     */
    fun getPiecesByColor(color: PieceColor): List<Piece> {
        return getAllPieces().filter { it.pieceColor == color }
    }

    /**
     * This function gets all the pieces on the board
     */
    fun getAllPieces(): MutableList<Piece> {
        val listOfPieces = mutableListOf<Piece>()
        board.forEach {
            it.forEach {
                it.piece?.let { listOfPieces.add(it) }
            }
        }
        return listOfPieces
    }

    /**
     * This function gets a piece from a specific position
     * @param row the row from we want the piece
     * @param col the column where we want the piece
     * @return gets the piece from the position if the position empty then returns null
     */
    fun getPiece(row: Int, col: Int): Piece? {
        return board[row][col].piece
    }

    /**
     * This function gets a piece from a specific position
     * @param pos the position of the piece
     * @return gets the piece from the position if the position empty then returns null
     */
    fun getPiece(pos: Pair<Int, Int>): Piece {
        val piece = board[pos.first][pos.second].piece
        return piece!!
    }

    /**
     * This function moves the pieces on the board. first it removes the piece from its old position then it puts it on the new position.
     * if the piece is null then nothing happens.
     * @param piece the piece we want to move
     * @param pos the position where we want to move the piece
     */
    fun movePiece(piece: Piece?, pos: Pair<Int, Int>) {
        //add piece
        board[pos.first][pos.second].piece = piece
        piece?.let {
            //remove piece
            board[piece.i][piece.j].piece = null

            //if pawn moves forward 2 it should be noted in fen
            enPassantParser(piece, pos)


            //changes the piece position
            it.step(pos.first, pos.second)

            //if rook is captured or moved revoke castling right
            revokeCastlingRightIfRookAbsent(scanCorners())

            //if King steps castling rights should be removed
            revokeCastlingRightIfKingStep(piece)

        }
    }

    // TODO decide if we need this
    /**
     * after the pieces are loaded in this function adds the pieces to their place on the board.
     */
    fun addPiece(piece: Piece) {
        board[piece.i][piece.j] = TileNew(false, piece)
    }

    fun removePiece(piece: Piece) {
        board[piece.i][piece.j] = TileNew(false, null)

    }

    fun loadBoard(pieces: List<Piece>){
        cleanTheBoard()
        pieces.forEach {
            addPiece(it)
        }
        //scan the corners if whether the rook is absent
        revokeCastlingRightIfRookAbsent(scanCorners())
    }


    /**
     * this function gives back if a specific color has castling right
     * @param color the pieceColor which we want to know if it can still castle
     * @return returns true if the color still has castling right
     */
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

    /**
     * this function saves the en passant possibility state
     *
     * @param piece the piece we want to move
     * @param pos the position where we want to move the piece
     */
    fun enPassantParser(piece: Piece, pos: Pair<Int, Int>) {
        if ((pos.first - piece.i).absoluteValue == 2 && piece.name == PieceName.PAWN) {
            when(piece.pieceColor) {
                PieceColor.WHITE -> {
                    possibleEnPassantTargets = BoardCoordinates.getTileName(pos.first + 1, pos.second)
                }
                PieceColor.BLACK -> {
                    possibleEnPassantTargets = BoardCoordinates.getTileName(pos.first - 1, pos.second)
                }
            }
        } else {possibleEnPassantTargets = "-"}
    }

    /**
     * This function revokes castling right if the rooks are absent
     * @param corners the corners state
     */
    fun revokeCastlingRightIfRookAbsent(corners: CastlingRights) {
        for (i in 0..3) {
            if (!corners[i]) {
                castlingRights[i] = false
            }
        }
    }

    /**
     * This function removes castling right if king steps
     *
     * @param piece the piece we want to move
     * @param pos the position where we want to move the piece
     */
    fun revokeCastlingRightIfKingStep(piece: Piece) {
        if (piece.name == PieceName.KING) {
            when (piece.pieceColor) {
                PieceColor.WHITE -> {
                    castlingRights.kw = false
                    castlingRights.qw = false
                }

                PieceColor.BLACK -> {
                    castlingRights.kb = false
                    castlingRights.qb = false
                }
            }
        }
    }

    /**
     * This function helps in debug. gives a graphical representation of the board.
     * @return the graphical representation of the board.
     */

    fun printBoard(): String {
        var boardString = ""

        board.forEach {
            it.forEach { tile ->
                boardString += tile.piece?.letter ?: if(!tile.free) {"-"} else {"X"}
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