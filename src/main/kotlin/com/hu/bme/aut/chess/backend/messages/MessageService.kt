package com.hu.bme.aut.chess.backend.messages

import com.hu.bme.aut.chess.backend.messages.DTO.MessageRequestDTO
import com.hu.bme.aut.chess.backend.users.User
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
    fun findAllMessages(): List<Message> {
        return messageRepository.findAll()
    }

    fun findMessageById(id: Long): Message? {
        return messageRepository.findById(id).orElse(null)
    }

    /**
     * Saves a message in to the database
     *
     * @param messageRequestDTO The data of the message
     */

    fun saveMessage(
        messageRequestDTO: MessageRequestDTO,
        user: User? = null
    ): Message? {
        val sender: User = userService.findAuthenticatedUser() ?: user!!
        val receiver = userService.findUserById(messageRequestDTO.receiver_id)

        if (receiver != null && sender.getId() != receiver.getId()) {
            val message = Message(sender = sender, receiver = receiver, text = messageRequestDTO.text)
            return messageRepository.save(message)
        }
        return null
    }

    /**
     * This function gets the messages between 2 users
     * One user is the authenticated user, and we get the other user by its id
     *
     * @param id the id of the conversationPartner
     * @param user if the sender is not the authenticated user
     *
     **/

    fun findMessagesBetweenUsers(
        id: Long,
        user: User? = null
    ): List<Message> {
        val authenticatedUser = userService.findAuthenticatedUser() ?: user!!
        return authenticatedUser
            .getMessagesSent()
            .toMutableList()
            .apply {
                addAll(authenticatedUser.getMessagesReceived())
            }
            .filter { message ->
                message.getSender().getId() == id || message.getReceiver().getId() == id
            }
    }


    fun deleteMessage(id: Long) {
        messageRepository.deleteById(id)
    }
}