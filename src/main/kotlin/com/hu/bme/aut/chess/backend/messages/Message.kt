package com.hu.bme.aut.chess.backend.messages

import com.hu.bme.aut.chess.backend.users.User
import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import org.hibernate.annotations.FetchProfile
import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators

@Entity
@Setter
@Getter
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class Message (
    @Id
    @GeneratedValue
    private val id: Long?=null,


    @ManyToOne
    private val sender: User,

    @ManyToOne
    private val receiver:User,

    private val text: String,

    private val sentDate: LocalDateTime = LocalDateTime.now()
) {
    fun getId(): Long? = id

    fun getSender(): User = sender

    fun getReceiver(): User = receiver

    fun getText(): String = text

    fun getSentDate(): LocalDateTime = sentDate
}