package com.hu.bme.aut.chess.backend.users

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(
        private var userRepository: UserRepository,
    )
{
    @GetMapping
    fun getAllUser(): ResponseEntity<List<User>> {
        val users = userRepository.findAll()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<User> {
        return userRepository.findById(id)
                .map { ResponseEntity.ok(it)}
                .orElseGet { ResponseEntity.notFound().build() }
    }


    @PostMapping
    fun addUser(@RequestBody user: User): ResponseEntity<User> {
        userRepository.save(user).let {
            return ResponseEntity.ok(it)
        }
    }

    @DeleteMapping("/{id}")
    fun removeUser(@PathVariable userId: Long): ResponseEntity<String> {
        userRepository.deleteById(userId)
        return ResponseEntity.ok().build();
    }
}