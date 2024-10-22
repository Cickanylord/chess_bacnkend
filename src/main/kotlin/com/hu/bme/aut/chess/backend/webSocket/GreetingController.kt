package com.hu.bme.aut.chess.backend.webSocket

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/messages")
    fun send(message: String?): String {
        return "test"
    }
}