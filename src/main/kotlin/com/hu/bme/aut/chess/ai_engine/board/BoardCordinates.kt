package com.hu.bme.aut.chess.ai_engine.board

/**
 * This object represent the naming of the chess board tiles
 * @author Waldmann Tamás(EO229S)
 */


object BoardCoordinates {
    val columns = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
    val rows = listOf('1', '2', '3', '4', '5', '6', '7', '8')
    val chessboard = Array(8) { row ->
        Array(8) { col ->
            "${columns[col]}${rows[7 - row]}"
        }
    }

    fun getTileName(row: Int, col: Int): String {
        return chessboard[row][col]
    }

    fun getCoordinate(tileName: String): Pair<Int, Int> {
        //println(tileName)
        if(tileName == "-") return Pair(-1,-1)
//        require (this.toString().contains(tileName) && tileName.length == 2)
        return Pair(
            7 - rows.indexOf(tileName[1]),
            columns.indexOf(tileName[0])
        )
    }

    override fun toString(): String {
        var string: String = ""
        chessboard.forEach { it.forEach { string += it }; string += '\n' }
        return string
    }
}