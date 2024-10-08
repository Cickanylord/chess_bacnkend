package com.hu.bme.aut.chess.backend.users


enum class UserRole(private val displayName: String) {
    GUEST("GUEST"),
    ADMIN("ADMIN"),
    LEADER("LEADER"),
    USER("USER");

    override fun toString(): String {
        return displayName
    }
}