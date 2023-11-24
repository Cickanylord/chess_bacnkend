package com.hu.bme.aut.chess.repository.match

import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.domain.Match
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MatchService @Autowired constructor(private val matchRepository: MatchRepository) {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    fun findAll():MutableList<Match>? { return matchRepository.findAll() }
    fun getMatchByID(id: Long): Match? {
        val match = matchRepository.findById(id)
        return if (!match.isEmpty) match.get()
        else null
    }

    fun getMatches(): List<Match?> {
        val xd = entityManager?.createQuery("SELECT n FROM Match n ")?.resultList as List<Match?>
        xd?.forEach{
            println("xd:"+ (it as Match).board )
        }
        return xd
        return matchRepository.findAll()
    }

    fun getMatchesByUserID(user: ChessUser){
        //entityManager?.createNamedQuery()
    }


    @Transactional
    fun modify(match: Match): Match {
        val oldAd: Match? = match.matchId?.let { getMatchByID(it) }
        return matchRepository.save(match)
    }


    fun save(match: Match): Match { return matchRepository.save(match) }
}

