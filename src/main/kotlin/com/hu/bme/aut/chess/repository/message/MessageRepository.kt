package com.hu.bme.aut.chess.repository.message


import com.hu.bme.aut.chess.domain.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface MessageRepository : JpaRepository<Message, Long>