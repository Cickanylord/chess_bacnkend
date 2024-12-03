package com.hu.bme.aut.chess


import com.hu.bme.aut.chess.backend.messages.DTO.MessageRequestDTO
import com.hu.bme.aut.chess.backend.messages.Message
import com.hu.bme.aut.chess.backend.messages.MessageRepository
import com.hu.bme.aut.chess.backend.messages.MessageService
import com.hu.bme.aut.chess.backend.security.userDetails.UserDetailsImpl
import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserRepository
import com.hu.bme.aut.chess.backend.users.UserRole
import com.hu.bme.aut.chess.backend.users.UserService
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserRequestDTO
import com.hu.bme.aut.chess.backend.webSocket.ChatEndPoint
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class MessageServiceTest {

    @InjectMocks
    lateinit var messageService: MessageService

    @Mock
    lateinit var messageRepository: MessageRepository

    @Mock
    lateinit var userService: UserService

    @Mock
    lateinit var chatEndPoint: ChatEndPoint

    private val user1 = User().apply {
        setId(1L)
        setName("Bob")
    }

    private val user2 = User().apply {
        setId(2L)
        setName("Alice")
    }

    private val user3 = User().apply {
        setId(3L)
        setName("Carl")
    }

    private val message1 = Message(
        id = 1L,
        sender = user1,
        receiver = user2,
        text = "Hello"
    )

    private val message2 = Message(
        id = 2L,
        sender = user2,
        receiver = user1,
        text = "Hello"
    )

    private val message3 = Message(
        id = 3L,
        sender = user1,
        receiver = user2,
        text = "Bye"
    )

    private val message4 = Message(
        id = 4L,
        sender = user2,
        receiver = user1,
        text = "Bye"
    )

    private val message5 = Message(
        id = 5L,
        sender = user3,
        receiver = user1,
        text = "Hello"
    )

    private val messages = listOf(message1, message2, message3, message4, message4, message5)

    @BeforeEach
    fun setup() {
        user1.getMessagesSent()
    }

    @Test
    fun `should return all messages`() {
        Mockito.`when`(messageRepository.findAll()).thenReturn(messages)

        val result = messageService.findAllMessages()

        assertEquals(messages.size, result.size)
        assert(result.containsAll(messages))
    }

    @Test
    fun `should return message by id`() {
        Mockito.`when`(messageRepository.findById(1)).thenReturn(Optional.of(message1))

        val result = messageService.findMessageById(1)

        assertNotNull(result)
        assertEquals(result, message1)
    }

    @Test
    fun `should return null if id does not exist`() {
        Mockito.`when`(messageRepository.findById(-1)).thenReturn(Optional.empty())

        val result = messageService.findMessageById(-1)

        assertNull(result)
    }

    @Test
    fun `should return message if save is good`() {
        val messageRequest = MessageRequestDTO(
            receiverId = 2,
            text = "hello"
        )

        val message = Message(
            id = 1L,
            sender = user1,
            receiver = user2,
            text = "Hello"
        )

        //find users
        Mockito.`when`(userService.findAuthenticatedUser()).thenReturn(user1)
        Mockito.`when`(userService.findUserById(2)).thenReturn(user2)

        //save message
        Mockito.`when`(messageRepository.save((Mockito.any(Message::class.java)))).thenReturn(message)

        val result = messageService.saveMessage(messageRequest)

        assertEquals(message1, result)
    }

    @Test
    fun `should return null if sender equals receiver`() {
        val messageRequest = MessageRequestDTO(
            receiverId = 1,
            text = "hello"
        )

        //find users
        Mockito.`when`(userService.findAuthenticatedUser()).thenReturn(user1)
        Mockito.`when`(userService.findUserById(1)).thenReturn(user1)

        val result = messageService.saveMessage(messageRequest)

        assertNull(result)
    }

    @Test()
    fun `should throw  null pointer exception if sender is not authenticated`() {
        val messageRequest = MessageRequestDTO(
            receiverId = 2,
            text = "hello"
        )
        Mockito.`when`(userService.findAuthenticatedUser()).thenReturn(null)
        Mockito.`when`(userService.findUserById(2)).thenReturn(null)

        assertThrows<NullPointerException> {
            messageService.saveMessage(messageRequest)
        }
    }

    @Test
    fun `should return messages between user1 and user2`() {

    }
}