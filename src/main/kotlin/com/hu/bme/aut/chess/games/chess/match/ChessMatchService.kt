package com.hu.bme.aut.chess.games.chess.match

import com.hu.bme.aut.chess.backend.match.dataTransferObject.StepRequest
import com.hu.bme.aut.chess.backend.match.Match
import com.hu.bme.aut.chess.backend.match.MatchRepository
import com.hu.bme.aut.chess.backend.match.MatchService
import com.hu.bme.aut.chess.backend.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChessMatchService @Autowired constructor(
    private val matchRepository: MatchRepository,
    private val userService: UserService
) : MatchService(matchRepository, userService) {

    override fun updateMatch(step: StepRequest): Match? {
        findMatchById(step.match_id)?.let { match ->
            // TODO: verification of board by the rules of chess
            match.setBoard(step.board)
            return matchRepository.save(match)
        }
        return null
    }
}