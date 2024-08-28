package com.hu.bme.aut.chess.backend.users

import com.hu.bme.aut.chess.backend.messages.Message
import com.hu.bme.aut.chess.backend.users.security.UserRole
import jakarta.persistence.*
import lombok.Data
import lombok.Getter
import kotlin.jvm.Transient


@Entity
@Table(name = "users")
@Data
@Getter
class User(
    @Id
    @GeneratedValue
    private val id: Long?=null,
    private var name: String,
    private var password: String
) {

    @OneToMany(mappedBy = "sender", cascade = [CascadeType.ALL])
    private val messagesSent: MutableList<Message> = ArrayList<Message>()

    @OneToMany(mappedBy = "receiver", cascade = [CascadeType.ALL])
    private val messagesReceived: MutableList<Message> = ArrayList<Message>()




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

    fun getRoles(): Set<UserRole> = roles

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

