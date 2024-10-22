package com.hu.bme.aut.chess.games.chess.match

import ai_engine.board.BoardData
import ai_engine.board.BoardLogic
import com.hu.bme.aut.chess.backend.match.Match
import com.hu.bme.aut.chess.backend.match.MatchRepository
import com.hu.bme.aut.chess.backend.match.MatchService
import com.hu.bme.aut.chess.backend.match.dataTransferObject.StepRequest
import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserService
import com.hu.bme.aut.chess.backend.webSocket.MatchEndPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChessMatchService @Autowired constructor(
    private val matchRepository: MatchRepository,
    private val userService: UserService,
    private val matchEndPoint: MatchEndPoint
) : MatchService(matchRepository, userService) {

    override fun updateMatch(step: StepRequest): Match? {
        findMatchById(step.matchId)?.let { match ->
            val prevBoard = BoardLogic(BoardData(match.getBoard()))
            val authenticatedUser= userService.findAuthenticatedUser() ?: return null
            if (authenticatedUser == match.getChallenged() || authenticatedUser == match.getChallenger()) {
                if (prevBoard.getPossibleFENs().contains(step.board) ) {
                //if (true) {
                    match.setBoard(step.board)
                    val savedMatch = matchRepository.save(match)
                    matchEndPoint.sendMessage(savedMatch, listOf(savedMatch.getChallenged().getId(), savedMatch.getChallenger().getId()))
                    return savedMatch
                }
            }
        }
        return null
    }

    override fun finedMatchesBetweenTwoPlayers(partnerId: Long): List<Match> {
        val user = userService.findAuthenticatedUser() ?: return listOf()
        return (user.getChallenger() + user.getChallenged())
            .filter { match ->
                match.getChallenged().getId() == partnerId ||
                match.getChallenger().getId() == partnerId
            }
    }

    fun BoardLogic.getPossibleFENs(): List<String> {
        val fenList = mutableListOf<String>()
        val pieces = board.getPiecesByColor(board.activeColor)

        for (piece in pieces) {
            val legalMoves = getLegalMoves(piece)

            for (move in legalMoves) {
                val tmpBoard = BoardLogic(BoardData(board.toString()))

                tmpBoard.move(tmpBoard.board.getPiece(piece.position), move)
                fenList.add(tmpBoard.board.toString())
            }
        }
        return fenList
    }
}