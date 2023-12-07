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

        chessUser?.let {
            return ResponseEntity.ok(chessUser)
        }
        return ResponseEntity.notFound().build()
    }

    @GetMapping("/match/{id}")
    fun getMatch(@PathVariable id: Long): ResponseEntity<Match> {
        val match: Match? = matchService.getMatchByID(id)
        match?.let {
            return ResponseEntity.ok(match)
        }

        return ResponseEntity.notFound().build()
    }



    @PostMapping
    fun addUser(): ResponseEntity<Match>{
        val user = ChessUser("János")
        val user2 = ChessUser("másik János")

        val match = Match(players = listOf(user, user2), board = "RKQKR" )
        userService.save(user)
        userService.save(user2)
        matchService.save(match)

        return ResponseEntity.ok(match)
    }



    @PostMapping("/addUser")
    fun addUser(@RequestBody user: ChessUser): ResponseEntity<ChessUser> {
        val chessUser: ChessUser = userService.save(user)
        return ResponseEntity.ok(chessUser)
    }

    @GetMapping("deleteUser/{user_id}")
    fun removeUser(@PathVariable user_id: Long): ResponseEntity<String> {
        if(userService.deleteUser(user_id)){
            return ResponseEntity.ok("user deleted")
        }
        return ResponseEntity.notFound().build()
    }
}