package com.hu.bme.aut.chess.controller.message

import com.hu.bme.aut.chess.domain.Match
import com.hu.bme.aut.chess.domain.Message
import com.hu.bme.aut.chess.repository.message.MessageService
import com.hu.bme.aut.chess.repository.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/messages")
class MessageController @Autowired constructor(
    private val userService: UserService,
    private val messageService: MessageService
) {
    @PostMapping("/postMessage")
    fun addMatch(@RequestBody messageBody: MessageBody): ResponseEntity<Message> {
        if(messageBody.receiver_id != messageBody.sender_id && messageBody.text != "") {
            val receiver = userService.getUserByID(messageBody.receiver_id)
            val sender = userService.getUserByID(messageBody.sender_id)

            when {
                receiver != null && sender != null -> {
                   Message(messageBody.text, sender, receiver).let {
                       messageService.save(it)
                       return ResponseEntity.ok(it)
                   }
                }
            }
        }

        return  ResponseEntity.notFound().build()
    }

    @GetMapping("getMessage/{message_id}")
    fun getMessageByID(@PathVariable message_id: Long): ResponseEntity<Message> {
        messageService.getMessageByID(message_id)?.let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity.notFound().build()
    }

    @GetMapping("getMessage/{sender_id}/{receiver_id}")
    fun getMessagesByUser(@PathVariable sender_id: Long, @PathVariable receiver_id: Long): ResponseEntity<List<Message>> {
        messageService.getMessageByUser(sender_id, receiver_id).let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity.notFound().build()
    }
}