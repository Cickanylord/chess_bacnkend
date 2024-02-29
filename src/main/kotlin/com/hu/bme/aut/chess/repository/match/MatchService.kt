package com.hu.bme.aut.chess.repository.match

import com.hu.bme.aut.chess.ai_engine.board.BoardData
import com.hu.bme.aut.chess.ai_engine.board.BoardLogic
import com.hu.bme.aut.chess.controller.match.StepRequest
import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.domain.Match
import com.hu.bme.aut.chess.ai_engine.board.oldBoard.Board
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
        val board = BoardData(step.prevBoard)

        board.getPiecesByColor(board.activeColor).forEach {
            val piece = it
            BoardLogic(board).getLegalMoves(piece).forEach {
                val tmpBoard = BoardData(step.prevBoard)
                val tmpPiece = tmpBoard.getPiece(piece.i, piece.j)
                tmpBoard.movePiece(tmpPiece, it)
                println(tmpBoard.fen)
                if (step.board == tmpBoard.fen.toString()) {
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

