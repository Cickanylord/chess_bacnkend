package hu.bme.aut.android.monkeychess.board.pieces

import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece


data class Pawn(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
    override var side: Side,
    override var hasMoved: Boolean = false
) : Piece {

    override var position: Pair<Int, Int> = Pair(i,j)
    override val name: PieceName = PieceName.PAWN



    override fun step(i: Int, j: Int){
        this.i = i
        this.j = j
        this.position = Pair(i,j)
        hasMoved = true
    }

    override fun flip() {
        i = 7 - i
        j = 7 - j
        this.position = Pair(i,j)

        side = if(side==Side.DOWN) {
            Side.UP
        } else {
            Side.DOWN
        }

    }

    override fun getAllMoves(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(3) { mutableListOf<Pair<Int, Int>>() }

        if (side  == Side.UP) {

            steps[0].add(Pair(i + 1, j))
            if (i == 1) {
                steps[0].add(Pair(i + 2, j))
            }


            //side step
            steps[1].add(Pair(i + 1, j - 1))
            steps[2].add(Pair(i + 1, j + 1))
        }

        if (side  == Side.DOWN) {
            steps[0].add(Pair(i - 1, j))

            if (i == 6) {
                steps[0].add(Pair(i - 2, j))
            }


            steps[1].add(Pair(i - 1, j - 1))
            steps[2].add(Pair(i - 1, j + 1))
        }
        return dropOutOfBoardSteps(steps)
    }
}