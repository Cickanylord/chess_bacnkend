package com.hu.bme.aut.chess


import com.hu.bme.aut.chess.repository.match.MatchService
import com.hu.bme.aut.chess.repository.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController



@RestController
@SpringBootApplication


class ChessApplication {

	@GetMapping
	fun Hello(): ResponseEntity<String> {
		return ResponseEntity.ok("Hello World!")
	}

}

	fun main(args: Array<String>) {
		runApplication<ChessApplication>(*args)
	}
