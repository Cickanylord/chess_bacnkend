package com.hu.bme.aut.chess


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController



@RestController
@SpringBootApplication
class ChessApplication {



}
	fun main(args: Array<String>) {
		runApplication<ChessApplication>(*args)
	}
