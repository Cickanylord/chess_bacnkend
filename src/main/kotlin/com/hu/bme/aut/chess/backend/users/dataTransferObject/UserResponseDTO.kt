package com.hu.bme.aut.chess.backend.users.dataTransferObject

import com.hu.bme.aut.chess.backend.users.UserRole
import com.hu.bme.aut.chess.backend.users.profilePicture.ProfilePictureEntity


data class UserResponseDTO(
    val id: Long,
    val name: String,
    val roles: Set<UserRole>,
    val messagesSent: List<Long>,
    val messagesReceived: List<Long>,
    val challenger: List<Long>,
    val challenged: List<Long>,
    val friendList: List<Long>,
    val matchesWined: List<Long>,
    val matchesLost: List<Long>,
    val profilePicture: ByteArray?

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserResponseDTO

        if (id != other.id) return false
        if (name != other.name) return false
        if (roles != other.roles) return false
        if (messagesSent != other.messagesSent) return false
        if (messagesReceived != other.messagesReceived) return false
        if (challenger != other.challenger) return false
        if (challenged != other.challenged) return false
        if (friendList != other.friendList) return false
        if (matchesWined != other.matchesWined) return false
        if (matchesLost != other.matchesLost) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + roles.hashCode()
        result = 31 * result + messagesSent.hashCode()
        result = 31 * result + messagesReceived.hashCode()
        result = 31 * result + challenger.hashCode()
        result = 31 * result + challenged.hashCode()
        result = 31 * result + friendList.hashCode()
        result = 31 * result + matchesWined.hashCode()
        result = 31 * result + matchesLost.hashCode()
        return result
    }
}