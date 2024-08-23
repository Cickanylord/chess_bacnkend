package com.hu.bme.aut.chess.backend.messages


import com.hu.bme.aut.chess.backend.messages.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface MessageRepository : JpaRepository<Message, Long>