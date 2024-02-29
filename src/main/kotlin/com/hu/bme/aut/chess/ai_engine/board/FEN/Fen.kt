package com.hu.bme.aut.chess.ai_engine.board.FEN

import com.hu.bme.aut.chess.ai_engine.board.BoardData

data class Fen(val boardData: BoardData) {
    val activeColor: Char
        get() = boardData.activeColor.name[0].lowercaseChar()

    override fun toString(): String{
        //TODO en-passant + step counter
        return "${piecePlacement()} $activeColor ${castlingRights()} - 0 1"
    }

    fun piecePlacement(): String {
        var emptyTile = 0
        var boardFEN = ""


        boardData.board.forEach { row ->
            row.forEach { tile ->
                if(tile.piece != null) {
                    if(emptyTile > 0) {
                        boardFEN += emptyTile.toString()
                    }
                    emptyTile = 0

                    boardFEN += tile.piece?.letter
                } else {
                    emptyTile ++
                }

            }
            if(emptyTile > 0) {
                boardFEN += emptyTile.toString()
                emptyTile = 0
            }

            boardFEN += '/'
        }

        return boardFEN.dropLast(1)
    }

    fun castlingRights(): String {
        var castlingChars = ""
        if (boardData.castlingRights.kw) { castlingChars += 'K'}
        if (boardData.castlingRights.qw) { castlingChars += 'Q'}
        if (boardData.castlingRights.kb) { castlingChars += 'k'}
        if (boardData.castlingRights.qb) { castlingChars += 'q'}
        if (castlingChars.isBlank()) { return "-" }
        return castlingChars
    }
}

