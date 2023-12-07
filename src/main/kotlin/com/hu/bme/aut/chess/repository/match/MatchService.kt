package com.hu.bme.aut.chess.repository.match

import com.hu.bme.aut.chess.controller.match.StepRequest
import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.domain.Match
import hu.bme.aut.android.monkeychess.board.Board
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MatchService @Autowired constructor(private val matchRepository: MatchRepository) {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    fun findAll():MutableList<Match>? { return matchRepository.findAll() }
    fun getMatchByID(id: Long): Match?{
        val match = matchRepository.findById(id)
        return if (!match.isEmpty) match.get()
        else null
    }

    fun getMatches(): List<Match?> {
        return matchRepository.findAll()
    }

    fun getMatchesByUserID(user: ChessUser): MutableList<Match> {
        return (entityManager?.createQuery(
            "SELECT m FROM Match m JOIN m.players p WHERE p.id = ?1")
            ?.setParameter(1, user.getId())
            ?.resultList) as MutableList<Match>
    }

    @Transactional
    fun delete(match: Match) {
        matchRepository.delete(match)
    }

    @Transactional
    fun modify(step: StepRequest): Match? {
        val match = getMatchByID(step.match_id)

        if (match == null || match.board != step.prevBoard) {
            return null
        }

        var valid = false
        val board = Board(fenBoard = step.prevBoard)

        board.getPiecesbyColor(board.currentPlayerBoard).forEach {
            val piece = it
            board.getAvailableSteps(piece, piece.pieceColor, true ).forEach {
                val tmpBoard = Board(fenBoard = step.prevBoard)
                val tmpPiece = tmpBoard.getPiece(piece.i, piece.j)
                tmpBoard.step(tmpPiece, it.first, it.second)
                println(tmpBoard.createFEN())
                if (step.board == tmpBoard.createFEN()) {
                    valid = true
                }
            }
        }

        if (valid) {
            //println("valid: ${step.board}")
            match.board = step.board
            save(match)
            return match
        }

        return null
    }

    @Transactional
    fun deletAll() {
        matchRepository.deleteAll()
    }

    @Transactional
    fun save(match: Match): Match { return matchRepository.save(match) }
}

