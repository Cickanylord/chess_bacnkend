package com.hu.bme.aut.chess.backend.messages.DTO

import com.hu.bme.aut.chess.backend.messages.Message
import org.springframework.stereotype.Service
import java.util.function.Function

@Service
class MessageDTOMapper: Function<Message, MessageDTO> {
    override fun apply(message: Message): MessageDTO {
        return MessageDTO(
            message.getId()!!,
            message.getSender().getId()!!,
            message.getReceiver().getId()!!,
            message.getText(),
            message.getSentDate()
        )
    }
}