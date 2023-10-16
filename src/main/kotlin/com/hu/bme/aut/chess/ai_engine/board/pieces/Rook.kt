package hu.bme.aut.android.monkeychess.board.pieces

import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece


data class Rook(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
    override var side: Side,
    override var hasMoved: Boolean = false
) : Piece {

    override val name: PieceName = PieceName.ROOK
    override var position: Pair<Int, Int> = Pair(i,j)

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

        if(side==Side.DOWN) {
            side = Side.UP
        }

        else{
            side=Side.DOWN
        }

    }

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(4) { mutableListOf<Pair<Int, Int>>() }

        for (counter in 0 until 8) {
            if (i + counter < 8)
                steps[0].add(Pair(i + counter, j))

            if (i - counter >= 0)
                steps[1].add(Pair(i - counter, j))

            if (j + counter < 8)
                steps[2].add(Pair(i, j + counter))

            if (j - counter >= 0)
                steps[3].add(Pair(i, j - counter))
        }
        return steps
    }
}