package com.hu.bme.aut.chess.backend.messages

import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/messages")
class MessageController @Autowired constructor(
        private val messageRepository: MessageRepository,
        private val userRepository: UserRepository
) {
        @GetMapping
        fun getAllMessages(): ResponseEntity<MutableList<Message>> {
                return ResponseEntity.ok(messageRepository.findAll())
        }

        @GetMapping("getMessage/{message_id}")
        fun getMessageByID(@PathVariable message_id: Long): ResponseEntity<Message> {
                return messageRepository.findById(message_id)
                        .map { ResponseEntity.ok(it)}
                        .orElseGet { ResponseEntity.notFound().build() }
        }

        @PostMapping()
        fun addMatch(@RequestBody messageBody: MessageBody): ResponseEntity<Message> {
                if(messageBody.receiver_id != messageBody.sender_id && messageBody.text != "") {
                        val receiver = userRepository.findById(messageBody.receiver_id)
                        val sender = userRepository.findById(messageBody.sender_id)

                        when {
                                receiver.isPresent && sender.isPresent -> {
                                        Message(sender = sender.get(), receiver = receiver.get(), text =  messageBody.text).let {
                                                return ResponseEntity.ok(messageRepository.save(it))
                                        }
                                }
                        }
                }
                return  ResponseEntity.notFound().build()
        }

}