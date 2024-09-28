package com.hu.bme.aut.chess.backend.messages

import com.hu.bme.aut.chess.backend.messages.DTO.MessageDTO
import com.hu.bme.aut.chess.backend.messages.DTO.MessageRequestDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/messages")
class MessageController @Autowired constructor(
        private val messageService: MessageService,
) {
        @GetMapping
        fun getAllMessages(): ResponseEntity<List<MessageDTO>> {
                return ResponseEntity.ok( messageService.findAllMessages().map { it.toMessageResponse()!! })
        }

        @GetMapping("/{id}")
        fun getMessageByID(@PathVariable id: Long): ResponseEntity<MessageDTO> =
                messageService.findMessageById(id).toMessageResponseEntity()

        @PostMapping("/partner/{id}")
        fun getMessageBetweenUsers(@PathVariable id: Long): ResponseEntity<List<MessageDTO?>> =
                ResponseEntity.ok(
                messageService
                        .findMessagesBetweenUsers(id)
                        .map { message ->
                                message.toMessageResponse()
                        }
                )



        @PostMapping()
        fun addMessage(@RequestBody messageRequestDTO: MessageRequestDTO): ResponseEntity<MessageDTO> =
                messageService.saveMessage(messageRequestDTO).toMessageResponseEntity()


        @DeleteMapping("/{id}")
        fun removeMessage(@PathVariable id: Long): ResponseEntity<String> {
                messageService.deleteMessage(id)
                return ResponseEntity.ok("Message Deleted")
        }

        fun Message?.toMessageResponse(): MessageDTO? {
                return this?.let { message ->
                        MessageDTO(
                                message.getId()!!,
                                message.getSender().getId()!!,
                                message.getReceiver().getId()!!,
                                message.getText(),
                                message.getSentDate()
                        )
                }
        }

        fun Message?.toMessageResponseEntity(): ResponseEntity<MessageDTO>  {
                return this.toMessageResponse()?.let {
                        ResponseEntity.ok(it)
                } ?: ResponseEntity.notFound().build()
        }
}