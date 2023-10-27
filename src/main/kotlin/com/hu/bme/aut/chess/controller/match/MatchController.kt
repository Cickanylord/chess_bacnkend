package com.hu.bme.aut.chess.controller.match

import com.hu.bme.aut.chess.domain.Match
import com.hu.bme.aut.chess.repository.match.MatchService
import com.hu.bme.aut.chess.repository.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/matches")
class MatchController @Autowired constructor(
    private val userService: UserService,
    private val matchService: MatchService
){
    @PostMapping("/addMatch")
    fun addMatch(@RequestBody match: MatchRequest): ResponseEntity<Match> {
        if(match.playerOne != match.playerTwo) {
            val user = userService.getUserByID(match.playerOne)
            val user2 = userService.getUserByID(match.playerTwo)
            when {
                user != null && user2 != null -> {
                    val match = Match(playerOne = user, playerTwo = user2, board = "RKQKR")
                    matchService.save(match)
                    return ResponseEntity.ok(match)
                }
            }
        }
        return ResponseEntity.notFound().build()
    }
}