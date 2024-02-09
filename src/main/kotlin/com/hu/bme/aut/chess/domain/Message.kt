package com.hu.bme.aut.chess.domain

import jakarta.persistence.*
import lombok.Data

@Table //is a corresponding table that matches that entity in the database
@Entity // for specifies class is an entity and is mapped to a database table.
@Data // for getter and setter
class Message(
    val text: String,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sender_id",
        joinColumns =  [jakarta.persistence.JoinColumn(name = "massage_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]

    )
    val sender: ChessUser,


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
        name = "receiver_id",
        joinColumns =  [jakarta.persistence.JoinColumn(name = "massage_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]

    )
    val receiver: ChessUser


) {
    @Id
    @GeneratedValue
    val messageId: Long? = null


    override fun toString(): String {
        super.toString()
        return "ID:${messageId}_SENDER:${sender}_RECEIVER:${receiver}_TEXT:$text"
    }

    override fun equals(other: Any?): Boolean {
        if(other is Message? ){
            return other.toString() == this.toString()
        }
        return super.equals(other)
    }

}