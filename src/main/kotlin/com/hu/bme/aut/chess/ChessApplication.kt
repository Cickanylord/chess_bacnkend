package com.hu.bme.aut.chess

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@RestController
class ChessApplication{

	@GetMapping
	fun Hello(): String{
		return "Hello World"
	}
}

fun main(args: Array<String>) {
	runApplication<ChessApplication>(*args)
}
