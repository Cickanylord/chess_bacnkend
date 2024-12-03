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
        return userRepository.findById(id).orElse(null)
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


    fun saveUser(userRequestDTO: UserRequestDTO): User? {
        if (userRepository.findUserByName(userRequestDTO.name) != null) return null
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
        findUserById(id)?.let {
            it.getRoles().add(role)
            saveUser(it)
            return it
        } ?: return null
    }

    fun addFriend(
        friendId: Long,
        user: User? = null,
    ): User? {
        val sender = findAuthenticatedUser() ?: user
        findUserById(friendId)?.let { friend ->
            if (friend != sender) {
                friend.getFriendList().add(sender!!)
                sender.getFriendList().add(friend)
                saveUser(friend)
                return saveUser(sender)
            }
        }
        return null
    }
    fun getAllFriends(): List<User> {
        val user = findAuthenticatedUser()
        return user?.getFriendList()!!.toList()
    }

    /**
     * This function deletes user by their id.
     * @param id the users id that shall be deleted
     *
     * This function is not tested!!!
     */
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