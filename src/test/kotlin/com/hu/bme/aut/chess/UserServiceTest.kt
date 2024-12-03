package com.hu.bme.aut.chess

import com.hu.bme.aut.chess.backend.security.userDetails.UserDetailsImpl
import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserRepository
import com.hu.bme.aut.chess.backend.users.UserRole
import com.hu.bme.aut.chess.backend.users.UserService
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserRequestDTO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    private val user1 = User().apply {
        setId(1L)
        setName("Bob")
    }
    private val user2 = User().apply {
        setId(2L)
        setName("Alice")
    }
    private val users = listOf(user1, user2)

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun `should return all users`() {
        Mockito.`when`(userRepository.findAll()).thenReturn(users)

        val result = userService.findAllUsers()

        assertEquals(2, result.size)
        Mockito.verify(userRepository).findAll()
    }

    @Test
    fun `should return user by id`() {
        val userId = user1.getId()!!

        Mockito.`when`(userRepository.findById(userId)).thenReturn(Optional.of(user1))

        val result = userService.findUserById(userId)

        assertNotNull(result)
        assertEquals(user1, result)
        Mockito.verify(userRepository).findById(userId)
    }

    @Test
    fun `should return null if user not found`() {
        val userId = -1L

        Mockito.`when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        val result = userService.findUserById(userId)

        assertNull(result)
    }

    @Test
    fun `should return user by name`() {
        val userName = user1.getName()
        Mockito.`when`(userRepository.findUserByName(userName)).thenReturn(user1)

        val result = userService.findUserByName(userName)

        assertNotNull(result)
        assertEquals(user1, result)
        Mockito.verify(userRepository).findUserByName(userName)
    }

    @Test
    fun `should return null if user not found by name`() {
        val userName = "no such user"
        Mockito.`when`(userRepository.findUserByName(userName)).thenReturn(null)

        val result = userService.findUserByName(userName)

        assertNull(result)
    }

    @Test
    fun `should find authenticated user`() {
        val userDetails = UserDetailsImpl(user1)
        val authentication = Mockito.mock(Authentication::class.java)
        val securityContext = Mockito.mock(SecurityContext::class.java)

        Mockito.`when`(authentication.principal).thenReturn(userDetails)
        Mockito.`when`(securityContext.authentication).thenReturn(authentication)
        SecurityContextHolder.setContext(securityContext)

        Mockito.`when`(userRepository.findById(user1.getId()!!)).thenReturn(Optional.of(user1))

        val authenticatedUser = userService.findAuthenticatedUser()

        assertEquals(user1, authenticatedUser)
    }

    @Test
    fun `should return null if no authenticated user`() {
        val securityContext = Mockito.mock(SecurityContext::class.java)
        Mockito.`when`(securityContext.authentication).thenReturn(null)
        SecurityContextHolder.setContext(securityContext)

        val authenticatedUser = userService.findAuthenticatedUser()

        assertNull(authenticatedUser)
    }


    @Test
    fun `should return saved user`() {
        val name = "asd"
        val password = "123"
        val encodedPassword = "Encoded123"
        val userRequest = UserRequestDTO(
            name = "asd",
            password = password
        )
        val newUser = User().apply {
            setId(3L)
            setName(name)
            setPassword(encodedPassword)
        }

        Mockito.`when`(passwordEncoder.encode(userRequest.password)).thenReturn("Encoded123")
        Mockito.`when`(userRepository.save(Mockito.any(User::class.java))).thenReturn(newUser)

        val result = userService.saveUser(userRequest)

        assertNotNull(result)
        assertEquals(newUser, result)
    }

    @Test
    fun `should grant authority`() {
        val userId = 1L
        val role = UserRole.ADMIN
        val user = User().apply {
            setId(userId)
            setRoles(UserRole.ADMIN)
        }

        Mockito.`when`(userService.saveUser(user)).thenReturn(user)

        val result = userService.saveUser(user)

        assert(result?.getRoles()?.contains(UserRole.ADMIN) ?: false)
        assertEquals(user, result)
    }
}