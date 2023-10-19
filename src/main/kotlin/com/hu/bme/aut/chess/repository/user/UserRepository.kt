package com.hu.bme.aut.chess.repository.user

import com.hu.bme.aut.chess.domain.User
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import org.springframework.transaction.annotation.Transactional





@Repository
interface UserRepository : JpaRepository<User,Long>
