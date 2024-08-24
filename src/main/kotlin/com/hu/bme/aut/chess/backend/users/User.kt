package com.hu.bme.aut.chess.backend.users

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.hu.bme.aut.chess.backend.messages.Message
import jakarta.persistence.*
import lombok.Data
import lombok.Getter
import kotlin.jvm.Transient
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators


@Entity
@Table(name = "users")
@Data
@Getter
class User(
    @Id
    @GeneratedValue
    private val id: Long?=null,
    private var name: String,
    @Transient
    private var password: String
) {
    @OneToMany(mappedBy = "sender")
    private val messagesSent: List<Message> = ArrayList<Message>()

    @OneToMany(mappedBy = "receiver")
    private val messagesReceived: List<Message> = ArrayList<Message>()


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

    override fun toString(): String {
        return "User(id=$id, name='$name')"
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

}

