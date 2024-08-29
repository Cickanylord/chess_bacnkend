package com.hu.bme.aut.chess.backend.users.DTO

import com.hu.bme.aut.chess.backend.users.User
import org.springframework.stereotype.Service
import java.util.function.Function


@Service
class UserResponseDTOMapper : Function<User, UserResponseDTO> {
    override fun apply(user: User): UserResponseDTO {
        return UserResponseDTO(
            user.getId()!!,
            user.getName(),
            user.getRoles(),
            user.getMessagesSent().map { it.getId()!! },
            user.getMessagesReceived().map { it.getId()!! },
            user.getChallenged().map {  it.getMatchId()!! },
            user.getChallenger().map {  it.getMatchId()!! },
        )
    }
}

