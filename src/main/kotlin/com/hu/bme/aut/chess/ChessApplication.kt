package com.hu.bme.aut.chess


import com.hu.bme.aut.chess.backend.match.MatchService
import com.hu.bme.aut.chess.backend.match.dataTransferObject.MatchRequestDTO
import com.hu.bme.aut.chess.backend.messages.DTO.MessageRequestDTO
import com.hu.bme.aut.chess.backend.messages.MessageService
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserRequestDTO
import com.hu.bme.aut.chess.backend.users.UserService
import com.hu.bme.aut.chess.backend.users.UserRole
import com.hu.bme.aut.chess.games.chess.match.ChessMatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
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
	private val messageService: MessageService,
	private val chessMatchService: ChessMatchService
) : CommandLineRunner {

	@Throws(java.lang.Exception::class)
	override fun run(vararg args: String) {
		val users: MutableList<UserRequestDTO> = ArrayList()
		val admin = UserRequestDTO(name = "admin", password = "demo")
		users.add(admin)

		val names = arrayOf("Bob", "Alice", "George", "Emily", "Ethan", "Lily", "Michael", "Sophia", "Karen", "Olivia")
		for (name in names) {
			UserRequestDTO(name = name, password = "demo").let { users.add(it) }
		}
		val savedUsers = users
			.map {userService.saveUser(it) }


		gibberishMessages
			.apply { shuffle() }
			.forEach {
				messageService.saveMessage(
					user = savedUsers[1],
					messageRequestDTO = MessageRequestDTO(
						receiverId = 4,
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
						receiverId = 2,
						text = it
					)
				)
			}

		userService.addFriend(savedUsers[1].getId()!!, savedUsers[3])
		userService.addFriend(savedUsers[1].getId()!!, savedUsers[4])
		userService.addFriend(savedUsers[1].getId()!!, savedUsers[5])
		userService.addFriend(savedUsers[1].getId()!!, savedUsers[6])

		for (i in 0..100) {
			chessMatchService.saveMatch(
				MatchRequestDTO(
					challenged = 6,
					board = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
				),
				savedUsers[1]
			)
		}
		chessMatchService.finishMatch(1,savedUsers[1], savedUsers[5])
		userService.grantAuthority(1L, UserRole.ADMIN)
	}
}
fun main(args: Array<String>) {
	runApplication<ChessApplication>(*args)
}

private val gibberishMessages = mutableListOf(
	"Zaluf mekto polo! Zaluf mekto polo! Zaluf mekto polo! Zaluf mekto polo! Zaluf mekto polo! Zaluf mekto polo! Zaluf mekto polo!" +
			"Zaluf mekto polo!" +
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