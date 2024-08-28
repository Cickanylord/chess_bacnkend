package com.hu.bme.aut.chess.backend.messages

import com.hu.bme.aut.chess.backend.messages.DTO.MessageRequestDTO
import com.hu.bme.aut.chess.backend.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MessageService @Autowired constructor(
    private val messageRepository: MessageRepository,
    private val userService: UserService
) {
    fun getAllMessages(): List<Message> {
        return messageRepository.findAll()
    }

    fun getMessageById(id: Long): Message? {
        return messageRepository.findById(id).orElse(null)
    }

    fun saveMessage(messageRequestDTO: MessageRequestDTO): Message? {
        val receiver = userService.getUserById(messageRequestDTO.receiver_id)
        val sender = userService.getUserById(messageRequestDTO.sender_id)

        if (receiver != null && sender != null && sender.getId() != receiver.getId()) {
            val message = Message(sender = sender, receiver = receiver, text = messageRequestDTO.text)
            return messageRepository.save(message)
        }
        return null
    }

    fun deleteMessage(id: Long) {
        messageRepository.deleteById(id)
    }
}