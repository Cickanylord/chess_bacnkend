package com.hu.bme.aut.chess




import ai_engine.board.BoardData
import ai_engine.board.BoardLogic
import ai_engine.board.pieces.enums.PieceColor
import ai_engine.util.CastlingRights
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest


//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BoardTest {
    lateinit var boardData: BoardData

    /**
     * the tables can be viewed on the website
     * @link https://www.dailychess.com/chess/chess-fen-viewer.php
     */

    val newBoardFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    val whiteKingHasCastlingRightFen = "4k3/7Q/8/8/8/8/8/R3K2R w KQ - 0 1"
    val blackKingHasCastlingRightFen = "r3k2r/8/8/8/8/8/8/R3K2R b kq - 0 1"
    val noCastlingFen = "r3k2r/8/8/8/8/8/8/R3K2R b - - 0 1"
    val checkScanForMovementFen = "8/8/8/3q4/8/1k6/3Q4/3K4 w - - 0 1"
    val whiteKingInCheck = "4k3/8/8/8/Q7/8/4q3/R3K2R w KQ - 0 1"
    val whiteCheckBlocking = "4k3/8/4q3/8/8/8/Q7/4K2R w K - 0 1"
    val rookCapture = "rn2kbnr/pBp1pppp/4b3/4q3/6P1/2N5/PPPPPP1P/R1BQK2R w KQkq - 0 1"
    @BeforeEach
    fun setupBoard() {
        //boardData = BoardData().apply { newGame() }
    }

    /**
     * test Fen generating
     */
    @Test
    fun fenInit() {
        //keeps all the castling rights
        assert(BoardData(newBoardFen).toString() == newBoardFen)

        //only white has castlingRight
        assert(BoardData(whiteKingHasCastlingRightFen).toString() == whiteKingHasCastlingRightFen)

        //only black has castlingRight


        //no one has castling right


        //TODO

        //EN-Passant

    }
    /**
     * Tets if invalid FEN string are sorted
     */
    @Test
    fun invalidFen() {
        assert(true)
    }

    /**
     * test castling reading
     */
    @Test
    fun readCastling() {
        //no one has castling
        assert( BoardData(noCastlingFen).castlingRights == CastlingRights(false,false,false,false))
        //white has castling
        assert( BoardData(whiteKingHasCastlingRightFen).castlingRights == CastlingRights(true,true,false,false))
        //black has castling
        assert( BoardData(blackKingHasCastlingRightFen).castlingRights == CastlingRights(false,false,true,true))
        //every player has castling
        assert( BoardData(newBoardFen).castlingRights == CastlingRights(true,true,true,true))
    }

    @Test
    fun writingCastling() {
        assert( BoardData(noCastlingFen).toString() == noCastlingFen)
        //white has castling
        assert( BoardData(whiteKingHasCastlingRightFen).toString() == whiteKingHasCastlingRightFen)
        //black has castling
        assert( BoardData(blackKingHasCastlingRightFen).toString() == blackKingHasCastlingRightFen)
        //every player has castling
        assert( BoardData(newBoardFen).toString() == newBoardFen)
    }

    /**
     * test color reading
     */
    @Test
    fun colorReading() {
        //black reading
        assert( BoardData(blackKingHasCastlingRightFen).activeColor == PieceColor.BLACK)
        //white reading
        assert( BoardData(whiteKingHasCastlingRightFen).activeColor == PieceColor.WHITE)
    }


    /**
     * test if white rooks moves castling rights are revoked
     */
    @Test
    fun whiteRookSteps(){
        val boardData = BoardData(whiteKingHasCastlingRightFen)
        val listOfExpectedSteps: MutableList<Pair<Int, Int>> = mutableListOf(Pair(7,2), Pair(7,3), Pair(7,5), Pair(7,6), Pair(6,3), Pair(6,4), Pair(6,5))

        BoardLogic(boardData).let {
            //when withe king has castling right
            var selectedSteps =it.getLegalMoves(it.board.getPiece(7,4)!!)

            assert(listOfExpectedSteps.containsAll(selectedSteps) && selectedSteps.size == listOfExpectedSteps.size)
            assert(it.board.castlingRights == CastlingRights(true, true, false, false))

            //when queen side rook steps
            listOfExpectedSteps.remove(Pair(7,2))
            it.move(it.board.getPiece(7,0)!!, Pair(6, 0))
            selectedSteps =it.getLegalMoves(it.board.getPiece(7,4)!!)

            assert(listOfExpectedSteps.containsAll(selectedSteps) && selectedSteps.size == listOfExpectedSteps.size)
            assert(it.board.castlingRights == CastlingRights(true, false, false, false))

            //when king side rook steps
            listOfExpectedSteps.remove(Pair(7,6))
            it.move(it.board.getPiece(7,7)!!, Pair(6, 7))
            selectedSteps =it.getLegalMoves(it.board.getPiece(7,4)!!)

            assert(listOfExpectedSteps.containsAll(selectedSteps) && selectedSteps.size == listOfExpectedSteps.size)
            assert(it.board.castlingRights == CastlingRights(false, false, false, false))
        }
    }
    /**
     * test if white king moves castling rights are revoked
     */
    @Test
    fun whiteKingStep() {
        //when king steps
        BoardLogic(BoardData(whiteKingHasCastlingRightFen)).let {
            val listOfExpectedSteps: MutableList<Pair<Int, Int>> = mutableListOf(Pair(7,3), Pair(7,4), Pair(7,5), Pair(5,3), Pair(5,4), Pair(5,5), Pair(6,5), Pair(6,3))
            val king = it.board.getPiece(7,4)!!
            it.move(king, Pair(6, 4))
            val selectedSteps =it.getLegalMoves(it.board.getPiece(6,4)!!)

            println(it.board.printBoard())
            assert(king.position == Pair(6, 4))
            assert(it.board.castlingRights == CastlingRights(false, false, false, false))
            assert(listOfExpectedSteps.containsAll(selectedSteps) && selectedSteps.size == listOfExpectedSteps.size)
        }
    }

    /**
     * Test if white queen side castling works
     */
    @Test
    fun whiteQueenSideCastling() {
        BoardLogic(BoardData(whiteKingHasCastlingRightFen)).let {
            val king = it.board.getPiece(Pair(7, 4))!!
            val queenSideRook = it.board.getPiece(Pair(7, 0))!!


            it.move(it.board.getPiece(7,4)!!, Pair(7, 2))

            assert(it.board.castlingRights == CastlingRights(false, false, false, false))
            assert(king.position == Pair(7, 2) && queenSideRook.position == Pair(7, 3))

        }
    }

    /**
     * Test if white king side castling works
     */
    @Test
    fun whiteKingSideCastling() {
        BoardLogic(BoardData(whiteKingHasCastlingRightFen)).let {
            val king = it.board.getPiece(Pair(7, 4))!!
            val kingSideRook = it.board.getPiece(Pair(7, 7))!!


            it.move(it.board.getPiece(7,4)!!, Pair(7, 6))

            assert(it.board.castlingRights == CastlingRights(false, false, false, false))
            assert(king.position == Pair(7, 6) && kingSideRook.position == Pair(7, 5))

        }
    }

    /**
     * test if the piece can't step if it king gets in to check
     */
    @Test
    fun checkScanWhenMovingTest() {
        BoardLogic(BoardData(checkScanForMovementFen))
            .let { logic ->
            logic.board.getPiece(6,3)
                .let {
                    logic.getLegalMoves(it!!, )
            }
        }
    }

    /**
     * test if the king can't castle when it checked
     */
    @Test
    fun noCastlingWhenChecked() {
        BoardLogic(BoardData(whiteKingInCheck)).let {
            val king = it.board.getPiece(7,4)
            val castlingSteps = mutableListOf(Pair(7,2), Pair(7,6))
            assert(it.getLegalMoves(king!!).none { castlingSteps.contains(it) })
        }
    }
    /**
     * test if the pieces only get the steps which blocks checks
     */
    @Test
    fun checkBlocking() {
        BoardLogic(BoardData(whiteCheckBlocking)).let { boardLogic ->
            val expectedSteps = mutableListOf(Pair(6,4),Pair(2,4), Pair(7,3), Pair(7,5), Pair(6,3), Pair(6,5))
            val steps: MutableList<Pair<Int, Int>> = mutableListOf()
            boardLogic.board.getPiecesByColor(PieceColor.WHITE).forEach {
                steps.addAll(boardLogic.getLegalMoves(it))
            }
            assert(steps.containsAll(expectedSteps) && steps.size == expectedSteps.size)
        }
    }

    /**
     * Tets to check if the rook is taken the castling rights are revoked
     */
    @Test
    fun rookCapture() {
        BoardLogic(BoardData(rookCapture)).let { boardLogic ->
            val bishop = boardLogic.board.getPiece(1,1)!!
            boardLogic.move(bishop, Pair(0,0))
            assert(boardLogic.board.castlingRights == CastlingRights(true, true, true, false))
        }
    }

}