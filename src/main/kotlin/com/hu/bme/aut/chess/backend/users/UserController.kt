package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.users.DTO.UserDTO
import com.hu.bme.aut.chess.backend.users.DTO.UserDTOMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(
        private var userRepository: UserRepository,
        private var userDTOMapper: UserDTOMapper
    )
{
    @GetMapping
    fun getAllUser(): ResponseEntity<List<UserDTO>> {
        val users = userRepository.findAll()
        return ResponseEntity.ok(users.map { userDTOMapper.apply(it) })
    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserDTO> {
        return userRepository.findById(id)
            .map(userDTOMapper::apply)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun addUser(@RequestBody user: User): ResponseEntity<UserDTO> {
        return userRepository.save(user)
            .let { ResponseEntity.ok(userDTOMapper.apply(it)) }
    }

    @DeleteMapping("/{id}")
    fun removeUser(@PathVariable userId: Long): ResponseEntity<String> {
        userRepository.deleteById(userId)
        return ResponseEntity.ok().build();
    }
}