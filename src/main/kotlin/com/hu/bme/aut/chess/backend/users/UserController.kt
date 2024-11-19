package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserFriendRequestDTO
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

    @GetMapping("/friends")
    fun getFriends(): ResponseEntity<List<UserResponseDTO?>> =
        ResponseEntity.ok(
            userService.getAllFriends().map {
                it.toUserResponseDTO()
            }
        )

    @PostMapping
    fun addUser(@RequestBody userRequestDTO: UserRequestDTO): ResponseEntity<UserResponseDTO> =
        userService.saveUser(userRequestDTO).toUserResponseEntity()

    @PutMapping("/addFriend")
    fun addUserToFriendList(@RequestBody userFriendRequestDTO: UserFriendRequestDTO): ResponseEntity<UserResponseDTO> =
        userService.addFriend(userFriendRequestDTO.id).toUserResponseEntity()



    @DeleteMapping("/{id}")
    fun removeUser(@PathVariable id: Long): ResponseEntity<String> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }


    fun User?.toUserResponseEntity(): ResponseEntity<UserResponseDTO>  {
        return this.toUserResponseDTO()?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }
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
            user.getChallenger().map {  it.getMatchId()!! },
            user.getFriendList().map {  it.getId()!! },
            user.getMatchesWined().map {  it.getMatchId()!! },
            user.getMatchesLost().map {  it.getMatchId()!! },
            user.getProfilePicture()?.getContent()
        )
    }
}