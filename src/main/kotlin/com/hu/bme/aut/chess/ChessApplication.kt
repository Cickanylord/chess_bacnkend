package com.hu.bme.aut.chess


import com.hu.bme.aut.chess.backend.messages.DTO.MessageDTO
import com.hu.bme.aut.chess.backend.messages.DTO.MessageRequestDTO
import com.hu.bme.aut.chess.backend.messages.MessageService
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
	private val userService: UserService,

	@Autowired
	private val messageService: MessageService
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
		val savedUsers = users
			.map {userService.saveUser(it)
			}


		gibberishMessages
			.apply { shuffle() }
			.forEach {
				messageService.saveMessage(
					user = savedUsers[1],
					messageRequestDTO = MessageRequestDTO(
						receiver_id = 4,
						text = it
					)
				)
			}

		gibberishMessages
			.apply { shuffle() }
			.forEach {
				messageService.saveMessage(
					user = savedUsers[3],
					messageRequestDTO = MessageRequestDTO(
						receiver_id = 2,
						text = it
					)
				)
			}

		userService.grantAuthority(1L, UserRole.ADMIN)
	}
}
fun main(args: Array<String>) {
	runApplication<ChessApplication>(*args)
}

private val gibberishMessages = mutableListOf(
	"Zaluf mekto polo!",
	"Wibble wobble floop!",
	"Flim flam goo!",
	"Glip glop zibbity!",
	"Fizzle doo bop.",
	"Shabadoo wib wub.",
	"Kringle bleep blop!",
	"Whizzle frizzle zap!",
	"Blorf wizzle foo.",
	"Jibber jabber blop.",
	"Blip zap flop.",
	"Bloop zoop zibble.",
	"Tibber tabber flip.",
	"Zam foo gloob.",
	"Snarf doodle bloop.",
	"Flabble wibble doop!",
	"Tizzle flibber flop.",
	"Shmizzle womp bop.",
	"Glorp zibber zup.",
	"Zibble wibber shoop!",
	"Klimb zibble zorp.",
	"Noodle wibber fop.",
	"Fibble zorp frib!",
	"Flippity wib woo.",
	"Shloop wibble zop.",
	"Tizzle dibber zibb.",
	"Bloop zorp zim.",
	"Whizzle zop floo.",
	"Frabble snizzle bop.",
	"Kibble fibble florp."
)