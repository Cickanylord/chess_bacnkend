package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserRequestDTO
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserResponseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(
    private val userService: UserService,
) {
    @GetMapping
    fun getAllUser(): ResponseEntity<List<UserResponseDTO?>> =
        ResponseEntity.ok(
            userService.findAllUsers()
                .map { it.toUserResponseDTO() }
        )


    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponseDTO> =
        userService.findUserById(id).toUserResponseEntity()


    @GetMapping("/me")
    fun getById(): ResponseEntity<UserResponseDTO> =
        userService.findAuthenticatedUser().toUserResponseEntity()


    @PostMapping
    fun addUser(@RequestBody userRequestDTO: UserRequestDTO): ResponseEntity<UserResponseDTO> =
        userService.saveUser(userRequestDTO).toUserResponseEntity()



    @DeleteMapping("/{id}")
    fun removeUser(@PathVariable id: Long): ResponseEntity<String> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }

    fun User?.toUserResponseDTO(): UserResponseDTO? {
        return this?.let { user ->
            UserResponseDTO(
                user.getId()!!,
                user.getName(),
                user.getRoles(),
                user.getMessagesSent().map { it.getId()!! },
                user.getMessagesReceived().map { it.getId()!! },
                user.getChallenged().map {  it.getMatchId()!! },
                user.getChallenger().map {  it.getMatchId()!! }
            )
        }
    }

    fun User?.toUserResponseEntity(): ResponseEntity<UserResponseDTO>  {
        return this.toUserResponseDTO()?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }
}