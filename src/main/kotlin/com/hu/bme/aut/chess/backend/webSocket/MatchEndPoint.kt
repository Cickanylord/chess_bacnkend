package com.hu.bme.aut.chess.backend.webSocket

import com.google.gson.Gson
import com.hu.bme.aut.chess.backend.match.Match
import com.hu.bme.aut.chess.backend.match.toMatchResponseDTO
import jakarta.websocket.*
import jakarta.websocket.server.ServerEndpoint
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
@ServerEndpoint(value = "/match")
class MatchEndPoint {

    companion object {
        private val sessions: MutableSet<Session> = Collections.newSetFromMap(ConcurrentHashMap<Session, Boolean>())
        private val userSessions: MutableMap<Long, Session> = ConcurrentHashMap()
    }

    @OnOpen
    fun onOpen(session: Session) {
        val userid = session.queryString
        userSessions[userid.toLong()] = session
    }

    @OnClose
    fun onClose(session: Session) {
        userSessions.values.remove(session)
        sessions.remove(session)
    }

    @OnError
    fun onError(session: Session?, throwable: Throwable) {
        throwable.printStackTrace()
    }

    // Broadcast to all connected clients
    @OnMessage
    fun broadcastMessage(message: String) {
        //sendMessage(message, 1L)
    }

    fun sendMessage(match: Match, listOfUserId: List<Long?>) {

        val matchJson = Gson().toJson(match.toMatchResponseDTO())

        listOfUserId.forEach { userId ->
            val session = userSessions[userId]
            session?.basicRemote?.sendText(matchJson)
        }
    }
}
