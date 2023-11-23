package com.hu.bme.aut.chess.controller.user

import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.domain.Match
import com.hu.bme.aut.chess.repository.match.MatchService
import com.hu.bme.aut.chess.repository.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
    private val userService: UserService,
    private val matchService: MatchService
    ){
    @GetMapping
    fun getAllUser(): ResponseEntity<List<ChessUser>> {
        val useres = userService.findAll()
        return if(useres == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(useres)
        }
    }

    @GetMapping("/id/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ChessUser> {
        val chessUser: ChessUser? = userService.getUserByID(id)
        return if (chessUser == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(chessUser)
        }
    }

    @GetMapping("/match/{id}")
    fun getMatch(@PathVariable id: Long): ResponseEntity<Match> {
        val match: Match? = matchService.getMatchByID(id)
        return if (match == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(match)
        }
    }

    @PostMapping
    fun addUser(): ResponseEntity<ChessUser>{
        val user = ChessUser("János")
        val user2 = ChessUser("másik János")

        val match = Match(playerOne = user, playerTwo = user2, board = "RKQKR" )
        userService.save(user)
        userService.save(user2)

        matchService.save(match)
        return ResponseEntity.ok(user2)
    }

    @PostMapping("/addUser")
    fun addUser(@RequestBody user: ChessUser): ResponseEntity<ChessUser> {
        val chessUser: ChessUser = userService.save(user)
        return ResponseEntity.ok(chessUser)
    }
}