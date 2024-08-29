package com.hu.bme.aut.chess


import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserRequestDTO
import com.hu.bme.aut.chess.backend.users.UserService
import com.hu.bme.aut.chess.backend.users.UserRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

@SpringBootApplication
@EnableScheduling
class ChessApplication(
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
