package com.hu.bme.aut.chess

import com.hu.bme.aut.chess.backend.match.MatchRepository
import com.hu.bme.aut.chess.backend.messages.MessageRepository
import com.hu.bme.aut.chess.backend.users.UserRepository
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChessApplicationTests @Autowired constructor(
    private val userRepository: UserRepository,
    private val matchRepository: MatchRepository,
    private val messageRepository: MessageRepository
){

	/*
	lateinit var user: User
	lateinit var user2: User
	lateinit var user3: User
	lateinit var match: Match
	lateinit var match2: Match
	@BeforeAll
	fun setupVariables() {
		user = User(name = "János", password = "123")
		user2 = User("másik János")
		user3 = User("Hamrmadik János")
		match = Match()
		match2 = Match()
	}
	@BeforeEach
	fun setupDB() {
		messageRepository.deleteAll()
		matchRepository.deleteAll()
		userRepository.deleteAll()
		//Saving in to db
		userRepository.save(user)
		userRepository.save(user2)
		userRepository.save(user3)

		matchRepository.save(match)
		matchRepository.save(match2)
	}
	@Test
	fun userToMatch() {
		assert(match.players[0] == user)
		assert(match.players[1] == user2)
	}

	@Test
	fun saveUser() {
		val savedUser = userRepository.getUserByID(user.getId()?: -1)
		assert(user.toString() == savedUser?.toString())
	}

	@Test
	fun saveMatch() {
		val savedMatch = matchRepository.getMatchByID(match.matchId ?: -1)
		assert(savedMatch?.toString() == match.toString())
	}
	@Test
	fun step() {
		//"rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1"
		val savedMatch = matchRepository.getMatchByID(match.matchId ?: -1)
		val nextStep = StepRequest(savedMatch?.matchId ?: -1, savedMatch?.board ?: "", "rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1")
		matchRepository.modify(nextStep)
		val modifeid = matchRepository.getMatchByID(match.matchId ?: -1)
		assert((modifeid?.board ?: " ") == "rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1")
	}

	@Test
	fun invalidStep() {
		val savedMatch = matchRepository.getMatchByID(match.matchId ?: -1)
		val nextStep = StepRequest(savedMatch?.matchId ?: -1, savedMatch?.board ?: "", "rnbqkbnr/pppppppp/8/5Q2/8/8/PPPPPPPP/RNB1KBNR b KQkq - 0 1")
		matchRepository.modify(nextStep)
		val modifeid = matchRepository.getMatchByID(match.matchId ?: -1)
		assert((modifeid?.board ?: " ") != "rnbqkbnr/pppppppp/8/5Q2/8/8/PPPPPPPP/RNB1KBNR b KQkq - 0 1" && (modifeid?.board ?: " ") == nextStep.prevBoard)
	}

	@Test()
	fun invalidPreviousBoard() {
		val savedMatch = matchRepository.getMatchByID(match.matchId ?: -1)
		val nextStep = StepRequest(savedMatch?.matchId ?: -1, savedMatch?.board ?: "", "NOT A TABLE")
		matchRepository.modify(nextStep)
		val modifeid = matchRepository.getMatchByID(match.matchId ?: -1)
		assert((modifeid?.board ?: " ") != "NOT A TABLE" && (modifeid?.board ?: " ") == nextStep.prevBoard)
	}

	@Test
	fun deleteMatch() {
		val savedMatch = matchRepository.getMatchByID(match2.matchId ?: -1)
		assert(savedMatch?.toString() == match2.toString())
		assert(matchRepository.findAll()?.contains(match2) ?: false)

		matchRepository.delete(match2)

		assert(matchRepository.findAll()?.contains(match2)?.not() ?: false)
	}

	@Test
	fun getMatchesByUser(){
		assert(matchRepository.getMatchesByUserID(user).size == 2)
		assert(matchRepository.getMatchesByUserID(user2).size == 1)
		assert(matchRepository.getMatchesByUserID(user3).filter { it.players.contains(user3) }.size == 1)
	}

	@Test
	fun deleteUser() {
		assert(userRepository.findAll().contains(user))
		assert(matchRepository.getMatchesByUserID(user).filter { it.players.contains(user) }.size == 2)

		userRepository.deleteUser(user.getId() ?: -1)

		assert(matchRepository.getMatchesByUserID(user).filter { it.players.contains(user) }.isEmpty())
		assert(userRepository.findAll().filter { it.toString() == user.toString() }.isEmpty())
	}

	@Test
	fun messageTest() {

		val message = Message("szia", user, user2)
		messageRepository.save(message)
		val saved = messageRepository.getMessageByID(message.messageId ?: -1)

		assert(saved == message)
		assert( messageRepository.getMessageByUser(user.getId() ?: -1, user2.getId() ?: -1)[0] == message)
	}

	 */

}
