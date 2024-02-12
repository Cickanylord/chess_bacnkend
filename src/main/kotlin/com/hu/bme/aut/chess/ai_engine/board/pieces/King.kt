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

        if(side==Side.DOWN) {
            side = Side.UP
        }

        else{
            side=Side.DOWN
        }

    }

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(10) { mutableListOf<Pair<Int, Int>>() }
        //normal steps
        if(i + 1 < 8 ){
            steps[0].add(Pair(i + 1, j))

            if(j - 1 >= 0){
                steps[1].add(Pair(i + 1, j - 1))
            }

            if(j + 1 < 8 ){
                steps[2].add(Pair(i + 1, j + 1))
            }
        }

        if(j - 1 >= 0){
            steps[3].add(Pair(i, j - 1))
        }
        if(j + 1 < 8 ){
            steps[4].add(Pair(i, j + 1))
        }

        if(i - 1 >= 0 ){
            steps[5].add(Pair(i - 1, j))
            if(j - 1 >= 0)
                steps[6].add(Pair(i - 1, j - 1))
            if(j + 1 < 8 )
                steps[7].add(Pair(i - 1, j + 1))
        }


        //Castling
        //posible king places
        /*
        wu 7,3
        wd 7,4

        bu 0,4
        bd 7,3
         */


        val castlingRow = if (side == Side.UP) 0 else 7
        var castlingColumn = 1


        if (pieceColor == PieceColor.WHITE) {
            if (side == Side.DOWN && i == 7 && j == 3) { castlingColumn = 2 }
        } else {
            if (side == Side.UP) { castlingColumn = 2 }
        }


        steps[8].add(Pair(castlingRow, castlingColumn))
        steps[9].add(Pair(castlingRow, castlingColumn + 4))



        return steps
    }
}