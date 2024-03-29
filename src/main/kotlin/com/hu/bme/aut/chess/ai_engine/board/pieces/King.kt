package hu.bme.aut.android.monkeychess.board.pieces


import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece


data class King(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
    override var side: Side,
    override var hasMoved: Boolean = false
) : Piece {


    override val name: PieceName = PieceName.KING
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

        side = if (side==Side.DOWN) {
            Side.UP
        } else {
            Side.DOWN
        }
    }

    override fun getAllMoves(): Array<MutableList<Pair<Int, Int>>> {
        val steps = Array(10) { mutableListOf<Pair<Int, Int>>() }
        //normal steps
        steps[0].add(Pair(i + 1, j))
        steps[1].add(Pair(i + 1, j - 1))
        steps[2].add(Pair(i + 1, j + 1))
        steps[3].add(Pair(i, j - 1))
        steps[4].add(Pair(i, j + 1))
        steps[5].add(Pair(i - 1, j))
        steps[6].add(Pair(i - 1, j - 1))
        steps[7].add(Pair(i - 1, j + 1))
        //Castling
        val castlingRow = if (side == Side.UP) 0 else 7

        steps[8].add(Pair(castlingRow, j + 2))
        steps[9].add(Pair(castlingRow, j - 2))

        //returns all possible steps
        return dropOutOfBoardSteps(steps)
    }
}