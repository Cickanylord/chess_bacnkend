package com.hu.bme.aut.chess.backend.match


import com.hu.bme.aut.chess.backend.match.dataTransferObject.MatchRequestDTO
import com.hu.bme.aut.chess.backend.match.dataTransferObject.StepRequest
import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
abstract class MatchService @Autowired constructor(
    private val matchRepository: MatchRepository,
    private val userService: UserService
) {
    fun findAllMatches(): List<Match> {
        return matchRepository.findAll()
    }

    fun findMatchById(id: Long): Match? {
        return matchRepository.findById(id).orElse(null)
    }

    fun saveMatch(matchReq: MatchRequestDTO, user: User? = null): Match? {
        val challenger = userService.findAuthenticatedUser() ?: user
        val challenged = userService.findUserById(matchReq.challenged)

        if(challenged != challenger) {
            when {
                challenger != null && challenged != null -> {
                    val match = Match(
                        challenger = challenger,
                        challenged =  challenged,
                        board = matchReq.board
                    )
                    return matchRepository.save(match)
                }
            }
        }
        return null
    }

    fun finishMatch(matchId: Long, winner: User, loser: User): Match? {
        findMatchById(matchId)?.let { match ->
            val players = match.getPlayers()
            if (players.containsAll(listOf(winner, loser)) && match.getIsGoing()) {
                match.finishMatch(winner, loser)
                return matchRepository.save(match)
            }
        }
        return null
    }

    abstract fun updateMatch(step: StepRequest): Match?

    abstract fun finedMatchesBetweenTwoPlayers(partnerId: Long) : List<Match>

    fun deleteMatch(id: Long) {
        matchRepository.deleteById(id)
    }

    fun updateMatch(match: Match, updateRequest: Any): Match {
        TODO("Not yet implemented")
    }
}