package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.security.userDetails.UserDetailsImpl
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserRequestDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private var passwordEncoder: PasswordEncoder,
) {
    fun findAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun findUserById(id: Long): User? {
        return userRepository.findById(id).get()
    }

    fun findUserByName(name: String): User? {
        return userRepository.findUserByName(name)
    }

    fun findAuthenticatedUser(): User?{
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.principal is UserDetails) {
            val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl
            return findUserById(userDetails.id)
        }
        return null
    }


    fun saveUser(userRequestDTO: UserRequestDTO): User {
        val user = User()
        user.setPassword (
            passwordEncoder.encode (
                userRequestDTO.password
            )
        )

        user.setName(userRequestDTO.name)

        return userRepository.save(user)
    }

    fun saveUser(user: User?): User? {
        return user?.let { userRepository.save(it) }
    }

    fun grantAuthority(id: Long,role: UserRole): User? {
        findUserById(1)?.let {
            it.getRoles().add(role)
            return it
        } ?: return null
    }

    fun addFriend(friendId: Long): User? {
        val user = findAuthenticatedUser()
        findUserById(friendId)?.let {friend ->
            if (friend != user) {
                friend.getFriendList().add(user!!)
                user.getFriendList().add(friend)
                saveUser(friend)
                return saveUser(user)
            }
        }
        return null
    }
    fun getAllFriends(): List<User> {
        val user = findAuthenticatedUser()
        return user?.getFriendList()!!.toList()
    }

    fun deleteUser(id: Long) {
        val user = findUserById(id) ?: return
        val friendIds = user
            .getFriendList()
            .map { it.getId() }

        userRepository.findAllById(friendIds).forEach { friend ->
            friend.getFriendList().remove(user)
            saveUser(friend)
        }
    }
}