package com.hu.bme.aut.chess


import com.hu.bme.aut.chess.backend.users.DTO.UserRequestDTO
import com.hu.bme.aut.chess.backend.users.UserService
import com.hu.bme.aut.chess.backend.users.security.UserRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
class ChessApplication(
	@Autowired
	private val passwordEncoder: PasswordEncoder,
	@Autowired
	private val userService: UserService
) : CommandLineRunner {

	@Throws(java.lang.Exception::class)
	override fun run(vararg args: String) {
		val users: MutableList<UserRequestDTO> = ArrayList()
		val admin = UserRequestDTO(name = "admin", password = "demo")
		users.add(admin)

		val names = arrayOf("Bob", "Alice", "George", "Emily", "Jack", "Lily", "Michael", "Sophia", "Karen", "Olivia")
		for (name in names) {
			UserRequestDTO(name = name, password = "demo").let { users.add(it) }
		}
		users.forEach {userService.saveUser(it)}
		userService.grantAuthority(1L, UserRole.ADMIN)
	}


}
fun main(args: Array<String>) {
	runApplication<ChessApplication>(*args)
}
