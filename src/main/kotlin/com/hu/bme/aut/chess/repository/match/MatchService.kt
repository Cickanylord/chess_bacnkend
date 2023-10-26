package com.hu.bme.aut.chess.repository.match

import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.domain.Match
import com.hu.bme.aut.chess.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MatchService @Autowired constructor(private val matchRepository: MatchRepository) {
    fun findAll():MutableList<Match>? { return matchRepository.findAll() }
    fun getUserByID(id: Long): Match? {
        val user = matchRepository.findById(id)
        return if (!user.isEmpty) user.get()
        else null
    }
    fun save(match: Match): Match { return matchRepository.save(match) }
}

