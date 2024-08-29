package com.hu.bme.aut.chess.backend.messages

import com.hu.bme.aut.chess.backend.messages.DTO.MessageDTO
import com.hu.bme.aut.chess.backend.messages.DTO.MessageDTOMapper
import com.hu.bme.aut.chess.backend.messages.DTO.MessageRequestDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/messages")
class MessageController @Autowired constructor(
        private val messageService: MessageService,
        private val messageDTOMapper: MessageDTOMapper
) {
        @GetMapping
        fun getAllMessages(): ResponseEntity<List<MessageDTO>> {
                return ResponseEntity.ok( messageService.findAllMessages().map { messageDTOMapper.apply(it) })
        }

        @GetMapping("/{id}")
        fun getMessageByID(@PathVariable id: Long): ResponseEntity<MessageDTO> {
                return messageService.findMessageById(id)?.let {
                        ResponseEntity.ok(messageDTOMapper.apply(it))
                } ?: ResponseEntity.notFound().build()
        }


        @PostMapping()
        fun addMatch(@RequestBody messageRequestDTO: MessageRequestDTO): ResponseEntity<MessageDTO> {
                return messageService.saveMessage(messageRequestDTO)?.let {
                        ResponseEntity.ok(messageDTOMapper.apply(it))
                } ?: ResponseEntity.notFound().build()
        }

        @DeleteMapping("/{id}")
        fun removeMessage(@PathVariable id: Long): ResponseEntity<String> {
                messageService.deleteMessage(id)
                return ResponseEntity.ok("Message Deleted")
        }

}