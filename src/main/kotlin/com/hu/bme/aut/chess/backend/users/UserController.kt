package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.users.DTO.UserDTO
import com.hu.bme.aut.chess.backend.users.DTO.UserDTOMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(
    private val userService: UserService,
    private val userDTOMapper: UserDTOMapper
) {
    @GetMapping
    fun getAllUser(): ResponseEntity<List<UserDTO>> {
        return ResponseEntity.ok(userService.getAllUsers().map { userDTOMapper.apply(it) })

    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserDTO> {
        return userService.getUserById(id)?.let {
            ResponseEntity.ok(userDTOMapper.apply(it))
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun addUser(@RequestBody user: User): ResponseEntity<UserDTO> {
        val savedUser = userService.saveUser(user)
        return ResponseEntity.ok(userDTOMapper.apply (savedUser))
    }

    @DeleteMapping("/{id}")
    fun removeUser(@PathVariable id: Long): ResponseEntity<String> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }
}