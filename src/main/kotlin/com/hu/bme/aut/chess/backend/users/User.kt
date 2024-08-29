package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.match.Match
import com.hu.bme.aut.chess.backend.messages.Message
import com.hu.bme.aut.chess.backend.users.security.UserRole
import jakarta.persistence.*
import lombok.Data
import lombok.Getter


@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue
    private val id: Long?=null

    private lateinit var name: String

    private lateinit var password: String

    @OneToMany(mappedBy = "sender", cascade = [CascadeType.ALL])
    private val messagesSent: MutableList<Message> = ArrayList()

    @OneToMany(mappedBy = "receiver", cascade = [CascadeType.ALL])
    private val messagesReceived: MutableList<Message> = ArrayList()

    @OneToMany(mappedBy = "challenger", cascade = [CascadeType.ALL])
    private val challenger: MutableList<Match> = ArrayList()

    @OneToMany(mappedBy = "challenged", cascade = [CascadeType.ALL])
    private val challenged: MutableList<Match> = ArrayList()



    @ElementCollection(fetch = FetchType.EAGER)
    private val roles: MutableSet<UserRole> = mutableSetOf(UserRole.GUEST)

    fun getId(): Long? = id

    fun getName(): String = name

    fun setName(name: String) {
        this.name = name
    }

    fun getPassword(): String = password

    fun setPassword(password: String) {
        this.password = password
    }

    fun getMessagesSent(): List<Message> = messagesSent

    fun getMessagesReceived(): List<Message> = messagesReceived

    fun getChallenger(): List<Match> = challenger

    fun getChallenged(): List<Match> = challenged

    fun getRoles(): MutableSet<UserRole> = roles

    fun setRoles(userRole: UserRole) {
        this.roles.add(userRole)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "User(id=$id, name='$name', password='$password', messagesSent=$messagesSent, messagesReceived=$messagesReceived, roles=$roles)"
    }

}

