package com.hu.bme.aut.chess.backend.webSocket

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.server.standard.ServerEndpointExporter

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        @Bean
        fun serverEndpointExporter(): ServerEndpointExporter {
            return ServerEndpointExporter()
        }
    }
}