package com.hu.bme.aut.chess.controller.match

import com.hu.bme.aut.chess.ai_engine.board.BoardData
import com.hu.bme.aut.chess.domain.Match
import com.hu.bme.aut.chess.repository.match.MatchService
import com.hu.bme.aut.chess.repository.user.UserService
import com.hu.bme.aut.chess.ai_engine.board.oldBoard.Board
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/matches")
class MatchController @Autowired constructor(
    private val userService: UserService,
    private val matchService: MatchService
){
    @GetMapping
    fun getMatches(): ResponseEntity<List<Match?>> {
        return  ResponseEntity.ok(matchService.getMatches())
    }

    @PostMapping("/addMatch")
    fun addMatch(@RequestBody matchReq: MatchRequest): ResponseEntity<Match> {
        if(matchReq.playerOne != matchReq.playerTwo) {
            val user = userService.getUserByID(matchReq.playerOne)
            val user2 = userService.getUserByID(matchReq.playerTwo)
            when {
                user != null && user2 != null -> {
                    val match = Match(players = listOf(user, user2), board = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                    matchService.save(match)
                    return ResponseEntity.ok(match)
                }
            }
        }
        return ResponseEntity.notFound().build()
    }
    @PostMapping("/step")
    fun Step(@RequestBody step: StepRequest): ResponseEntity<Match> {
        val nextStep = matchService.modify(step)
        nextStep?.let { return ResponseEntity.ok(nextStep) }
        return ResponseEntity.notFound().build()
    }

    @GetMapping("deleteMatch/{match_id}")
    fun deleteMatch(@PathVariable match_id: Long): ResponseEntity<String> {
        val match = matchService.getMatchByID(match_id)
        match?.let {
            matchService.delete(match)
            return ResponseEntity.ok("Match Deleted")
        }
        return ResponseEntity.notFound().build()
    }
}