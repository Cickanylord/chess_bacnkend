package com.hu.bme.aut.chess.backend.messages



import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MessageService  @Autowired constructor(private val messageRepository: MessageRepository) {

    @PersistenceContext
    private val entityManager: EntityManager? = null
    fun findAll():MutableList<Message>? { return messageRepository.findAll() }
    fun getMessageByID(id: Long): Message?{
        val message = messageRepository.findById(id)
        return if (!message.isEmpty) message.get()
        else null
    }
    @Transactional
    fun save(message: Message): Message { return messageRepository.save(message) }

    @Transactional
    fun deletAll() {
        messageRepository.deleteAll()
    }

    fun getMessageByUser(sender_id: Long, receiver_id: Long):MutableList<Message> {
        return (entityManager?.createQuery(
            "SELECT m FROM Message m  WHERE m.receiver.id = ?1 AND m.sender.id = ?2")
            ?.setParameter(1,receiver_id)
            ?.setParameter(2,sender_id)
            ?.resultList) as MutableList<Message>
    }
}