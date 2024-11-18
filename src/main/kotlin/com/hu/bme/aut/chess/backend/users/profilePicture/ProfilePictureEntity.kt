package com.hu.bme.aut.chess.backend.users.profilePicture

import com.hu.bme.aut.chess.backend.users.User
import jakarta.persistence.*


@Entity
class ProfilePictureEntity {
    @Id
    @GeneratedValue
    private var id: Long? = null

    @Lob
    private lateinit var content: ByteArray

    @OneToOne(cascade = [(CascadeType.ALL)])
    private lateinit var owner: User


    fun getId(): Long = id ?: -1

    fun getOwner(): User = owner
    fun setOwner(user: User) {owner = user}

    fun getContent(): ByteArray = content

    fun setContent(content: ByteArray) {
        this.content = content
    }
}
