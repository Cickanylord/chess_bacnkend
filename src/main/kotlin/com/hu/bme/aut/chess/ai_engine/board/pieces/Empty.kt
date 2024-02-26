package hu.bme.aut.android.monkeychess.board.pieces

import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceName
import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.Side
import com.hu.bme.aut.chess.ai_engine.board.pieces.peice_interface.Piece


class Empty(
    override var i: Int,
    override var j: Int,
): Piece {



    override val name: PieceName = PieceName.EMPTY
    override var position: Pair<Int, Int> = Pair(0,0)
    override var side: Side
        get() = TODO("Not yet implemented")
        set(value) {}
    override fun step(i: Int, j: Int){
    }

    override fun getAllMoves(): Array<MutableList<Pair<Int, Int>>> {
        TODO("Not yet implemented")
    }

    override var hasMoved: Boolean = false
    override var pieceColor: PieceColor = PieceColor.EMPTY

    override fun flip() {
    }
}