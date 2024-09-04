package com.hu.bme.aut.chess.backend.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EmailService {
    @Autowired
    private val emailSender: JavaMailSender? = null
    @Async
    fun sendSimpleMailMessage(to: String, subject: String,text: String) {
        try {
            val message = SimpleMailMessage()
            message.subject = subject
            message.setTo(to)
            message.text = text
            emailSender!!.send(message)
        } catch (e: Exception) {

            throw RuntimeException(e.message)

        }
    }
}