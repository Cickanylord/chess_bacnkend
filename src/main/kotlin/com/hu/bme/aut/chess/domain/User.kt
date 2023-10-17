package com.hu.bme.aut.chess.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class User(
    @Id
    @GeneratedValue
    val id: Long,
    val userName: String,
)
