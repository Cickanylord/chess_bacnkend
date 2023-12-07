package com.hu.bme.aut.chess.repository.user

import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.domain.Match
import com.hu.bme.aut.chess.repository.match.MatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository, private val  matchService: MatchService) {


    fun findAll():MutableList<ChessUser> { return userRepository.findAll() }
    fun getUserByID(id: Long): ChessUser? {
        val user = userRepository.findById(id)
        return if (!user.isEmpty) user.get()
        else null
    }
    fun save(user: ChessUser):ChessUser { return userRepository.save(user) }

    fun deleteUser(user_id: Long): Boolean {
        val user = getUserByID(user_id)
        if (user != null) {
            matchService.getMatchesByUserID(user)?.forEach {
                println("Match id: "+(it as Match).matchId)
                matchService.delete(it)
            }
            userRepository.delete(user)
            return true
        }
        return false
    }

    @Transactional
    fun deletAll() {
        userRepository.deleteAll()
    }
}