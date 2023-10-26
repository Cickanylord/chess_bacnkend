package com.hu.bme.aut.chess.controller.user

import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor( private final val userService: UserService){

    @GetMapping
    fun getAllUser(): ResponseEntity<List<ChessUser>> {
        val useres = userService.findAll()
        if(useres.isNullOrEmpty()) {
            return ResponseEntity.notFound().build()
        } else {
            return ResponseEntity.ok(useres)
        }
    }

    @PostMapping
    fun addUser(){
        val user = ChessUser("János")
        val user2 = ChessUser("másik János")
        userService.save(user)
        userService.save(user2)
    }
}