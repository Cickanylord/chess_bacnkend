package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.users.DTO.UserRequestDTO
import com.hu.bme.aut.chess.backend.users.DTO.UserResponseDTO
import com.hu.bme.aut.chess.backend.users.DTO.UserResponseDTOMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(
    private val userService: UserService,
    private val userResponseDTOMapper: UserResponseDTOMapper
) {
    @GetMapping
    fun getAllUser(): ResponseEntity<List<UserResponseDTO>> {
        return ResponseEntity.ok(userService.getAllUsers().map { userResponseDTOMapper.apply(it) })

    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponseDTO> {
        return userService.getUserById(id)?.let {
            ResponseEntity.ok(userResponseDTOMapper.apply(it))
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun addUser(@RequestBody userRequestDTO: UserRequestDTO): ResponseEntity<UserResponseDTO> {
        val savedUser = userService.saveUser(userRequestDTO)
        return ResponseEntity.ok(userResponseDTOMapper.apply (savedUser))
    }

    @DeleteMapping("/{id}")
    fun removeUser(@PathVariable id: Long): ResponseEntity<String> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }
}