package com.hu.bme.aut.chess.controllers


import com.hu.bme.aut.chess.backend.users.*
import com.hu.bme.aut.chess.backend.users.DTO.UserDTO
import com.hu.bme.aut.chess.backend.users.DTO.UserDTOMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest @Autowired constructor (
    @InjectMocks
    private val userController: UserController,

    @Mock
    private val userRepository: UserRepository,

    @Mock
    private val userDTOMapper: UserDTOMapper

) {
    @Test
    fun getAllUsersTest() {
        val user = User(1L, "Bob", "123")
        val userDTO = UserDTO(1L, name = "Bob", messagesSent = listOf(0L), messagesReceived = listOf(0L))
        Mockito.`when`(userRepository.findAll()).thenReturn(listOf(user))
        Mockito.`when`(userDTOMapper.apply(user)).thenReturn(userDTO)

        val expected: List<UserDTO> = listOf(userDTO)
        val actual: List<UserDTO>? = userController.getAllUser().body

        Assertions.assertEquals(expected, actual)
    }
}


