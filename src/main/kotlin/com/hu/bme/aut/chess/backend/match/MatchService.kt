package com.hu.bme.aut.chess.backend.match


import com.hu.bme.aut.chess.backend.match.DTO.MatchRequestDTO
import com.hu.bme.aut.chess.backend.match.DTO.StepRequest
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
    fun getAllMatches(): List<Match> {
        return matchRepository.findAll()
    }

    fun getMatchById(id: Long): Match? {
        return matchRepository.findById(id).get()
    }

    fun saveMatch(matchReq: MatchRequestDTO): Match? {
        if(matchReq.challenged != matchReq.challenger) {
            val challenger = userService.getUserById(matchReq.challenger)
            val challenged = userService.getUserById(matchReq.challenged)
            when {
                challenger != null && challenged != null -> {
                    val match = Match(challenger = challenger, challenged =  challenged)
                    return matchRepository.save(match)
                }
            }
        }
        return null
    }

    abstract fun updateMatch(step: StepRequest): Match?

    fun deleteMatch(id: Long) {
        matchRepository.deleteById(id)
    }

    fun updateMatch(match: Match, updateRequest: Any): Match {
        TODO("Not yet implemented")
    }
}