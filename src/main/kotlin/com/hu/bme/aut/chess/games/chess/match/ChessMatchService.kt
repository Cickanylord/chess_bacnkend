package com.hu.bme.aut.chess.games.chess.match

import com.hu.bme.aut.chess.ai_engine.board.BoardData
import com.hu.bme.aut.chess.ai_engine.board.BoardLogic
import com.hu.bme.aut.chess.backend.match.Match
import com.hu.bme.aut.chess.backend.match.MatchRepository
import com.hu.bme.aut.chess.backend.match.MatchService
import com.hu.bme.aut.chess.backend.match.dataTransferObject.StepRequest
import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChessMatchService @Autowired constructor(
    private val matchRepository: MatchRepository,
    private val userService: UserService
) : MatchService(matchRepository, userService) {

    override fun updateMatch(step: StepRequest): Match? {
        findMatchById(step.matchId)?.let { match ->
            val prevBoard = BoardLogic(BoardData(match.getBoard()))
            println(prevBoard.getPossibleFENs())

            val authenticatedUser: User? = userService.findAuthenticatedUser()
            if (authenticatedUser == match.getChallenger() || authenticatedUser == match.getChallenged()) {
                match.setBoard(step.board)
                if (prevBoard.getPossibleFENs().contains(step.board) ) {
                    return matchRepository.save(match)
                }
            }
        }
        return null
    }

    fun BoardLogic.getPossibleFENs(): List<String> {
        val fenList = mutableListOf<String>()
        val pieces = board.getPiecesByColor(board.activeColor)

        for (piece in pieces) {
            val legalMoves = getLegalMoves(piece)

            for (move in legalMoves) {
                val tmpBoard = BoardLogic(BoardData(board.fen.toString()))

                tmpBoard.move(tmpBoard.board.getPiece(piece.position), move)
                fenList.add(tmpBoard.board.fen.toString())
            }
        }
        return fenList
    }
}