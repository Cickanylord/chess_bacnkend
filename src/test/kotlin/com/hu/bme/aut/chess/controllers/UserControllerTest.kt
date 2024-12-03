package com.hu.bme.aut.chess.controllers


import com.hu.bme.aut.chess.backend.users.UserController
import com.hu.bme.aut.chess.backend.users.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest @Autowired constructor (
    @InjectMocks
    private val userController: UserController,

    @Mock
    private val userRepository: UserRepository,
)


