package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserRequestDTO
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserResponseDTO
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserResponseDTOMapper
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
        return ResponseEntity.ok(userService.findAllUsers().map { it.toUserResponse() })

    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponseDTO> {
        return userService.findUserById(id)?.let {
            ResponseEntity.ok(it.toUserResponse())
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun addUser(@RequestBody userRequestDTO: UserRequestDTO): ResponseEntity<UserResponseDTO> {
        val savedUser = userService.saveUser(userRequestDTO)
        return ResponseEntity.ok(savedUser.toUserResponse())
    }

    @DeleteMapping("/{id}")
    fun removeUser(@PathVariable id: Long): ResponseEntity<String> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }

    fun User.toUserResponse(): UserResponseDTO {
        return userResponseDTOMapper.apply(this)
    }
}