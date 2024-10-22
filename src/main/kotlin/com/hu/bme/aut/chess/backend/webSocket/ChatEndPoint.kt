package com.hu.bme.aut.chess.backend.webSocket

import jakarta.websocket.*
import jakarta.websocket.server.ServerEndpoint
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
@ServerEndpoint(value = "/chat")
class ChatEndPoint {

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

    fun sendMessage(message: String, listOfUserId: List<Long?>) {
        listOfUserId.forEach { userId ->
            val session = userSessions[userId]
            session?.basicRemote?.sendText(message)
        }
    }
}
