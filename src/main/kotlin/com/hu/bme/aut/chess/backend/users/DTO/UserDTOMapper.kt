package com.hu.bme.aut.chess.backend.users.DTO

import com.hu.bme.aut.chess.backend.users.User
import org.springframework.stereotype.Service
import java.util.function.Function


@Service
class UserDTOMapper : Function<User, UserDTO> {
    override fun apply(user: User): UserDTO {
        return UserDTO(
            user.getId()!!,
            user.getName(),
            user.getMessagesSent().map { it.getId()!! },
            user.getMessagesReceived().map { it.getId()!! },
        )
    }
}

