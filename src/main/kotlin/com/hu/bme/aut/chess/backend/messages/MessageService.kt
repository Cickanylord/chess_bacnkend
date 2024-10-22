package com.hu.bme.aut.chess.backend.messages

import com.google.gson.Gson
import com.hu.bme.aut.chess.backend.messages.DTO.MessageRequestDTO
import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserService
import com.hu.bme.aut.chess.backend.webSocket.ChatEndPoint

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MessageService @Autowired constructor(
    private val messageRepository: MessageRepository,
    private val userService: UserService,
    private val chatEndPoint: ChatEndPoint
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
        val receiver = userService.findUserById(messageRequestDTO.receiverId)

        if (receiver != null && sender.getId() != receiver.getId()) {
            val message = Message(sender = sender, receiver = receiver, text = messageRequestDTO.text)
            val savedMessage = messageRepository.save(message)

            chatEndPoint.sendMessage(
                message = Gson().toJson(savedMessage.toMessageResponse()),
                listOfUserId = listOf(
                    receiver.getId(),
                    sender.getId(),
                )
            )

            return savedMessage
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