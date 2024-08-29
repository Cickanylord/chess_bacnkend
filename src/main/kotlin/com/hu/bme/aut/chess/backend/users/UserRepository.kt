package com.hu.bme.aut.chess.backend.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User,Long> {
    fun findUserByName(name: String): User?
}
