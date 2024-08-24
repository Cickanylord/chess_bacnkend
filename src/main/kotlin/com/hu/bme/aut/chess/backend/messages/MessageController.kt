package com.hu.bme.aut.chess.backend.messages

import com.hu.bme.aut.chess.backend.messages.DTO.MessageDTO
import com.hu.bme.aut.chess.backend.messages.DTO.MessageDTOMapper
import com.hu.bme.aut.chess.backend.messages.DTO.MessageRequestDTO
import com.hu.bme.aut.chess.backend.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/messages")
class MessageController @Autowired constructor(
        private val messageRepository: MessageRepository,
        private val userRepository: UserRepository,
        private val messageDTOMapper: MessageDTOMapper
) {
        @GetMapping
        fun getAllMessages(): ResponseEntity<List<MessageDTO>> {
                return ResponseEntity.ok( messageRepository.findAll().map { messageDTOMapper.apply(it) })
        }

        @GetMapping("getMessage/{message_id}")
        fun getMessageByID(@PathVariable message_id: Long): ResponseEntity<MessageDTO> {
                return messageRepository.findById(message_id)
                        .map ( messageDTOMapper::apply )
                        .map { ResponseEntity.ok(it)}
                        .orElseGet { ResponseEntity.notFound().build() }
        }

        @PostMapping()
        fun addMatch(@RequestBody messageRequestDTO: MessageRequestDTO): ResponseEntity<MessageDTO> {
                if(messageRequestDTO.receiver_id != messageRequestDTO.sender_id && messageRequestDTO.text != "") {
                        val receiver = userRepository.findById(messageRequestDTO.receiver_id)
                        val sender = userRepository.findById(messageRequestDTO.sender_id)

                        when {
                                receiver.isPresent && sender.isPresent -> {
                                        Message(sender = sender.get(), receiver = receiver.get(), text =  messageRequestDTO.text).let {
                                                return ResponseEntity.ok(messageDTOMapper.apply(messageRepository.save(it)))
                                        }
                                }
                        }
                }
                return  ResponseEntity.notFound().build()
        }

}