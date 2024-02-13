package com.hu.bme.aut.chess

import com.hu.bme.aut.chess.controller.match.StepRequest
import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.domain.Match
import com.hu.bme.aut.chess.domain.Message
import com.hu.bme.aut.chess.repository.match.MatchService
import com.hu.bme.aut.chess.repository.message.MessageService
import com.hu.bme.aut.chess.repository.user.UserService
import hu.bme.aut.android.monkeychess.board.Board
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChessApplicationTests @Autowired constructor(
	private val userService: UserService,
	private val matchService: MatchService,
	private val messageService: MessageService
){
	lateinit var user: ChessUser
	lateinit var user2: ChessUser
	lateinit var user3: ChessUser
	lateinit var match: Match
	lateinit var match2: Match
	@BeforeAll
	fun setupVariables() {
		user = ChessUser("János")
		user2 = ChessUser("másik János")
		user3 = ChessUser("Hamrmadik János")
		match = Match(players = listOf(user, user2), board = Board("").createFEN() )
		match2 =Match(players = listOf(user, user3), board = Board("").createFEN() )
	}
	@BeforeEach
	fun setupDB() {
		messageService.deletAll()
		matchService.deletAll()
		userService.deletAll()
		//Saving in to db
		userService.save(user)
		userService.save(user2)
		userService.save(user3)

		matchService.save(match)
		matchService.save(match2)
	}
	@Test
	fun userToMatch() {
		assert(match.players[0] == user)
		assert(match.players[1] == user2)
	}

	@Test
	fun saveUser() {
		val savedUser = userService.getUserByID(user.getId()?: -1)
		assert(user.toString() == savedUser?.toString())
	}

	@Test
	fun saveMatch() {
		val savedMatch = matchService.getMatchByID(match.matchId ?: -1)
		assert(savedMatch?.toString() == match.toString())
	}
	@Test
	fun step() {
		//"rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1"
		val savedMatch = matchService.getMatchByID(match.matchId ?: -1)
		val nextStep = StepRequest(savedMatch?.matchId ?: -1, savedMatch?.board ?: "", "rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1")
		matchService.modify(nextStep)
		val modifeid = matchService.getMatchByID(match.matchId ?: -1)
		assert((modifeid?.board ?: " ") == "rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1")
	}

	@Test
	fun invalidStep() {
		val savedMatch = matchService.getMatchByID(match.matchId ?: -1)
		val nextStep = StepRequest(savedMatch?.matchId ?: -1, savedMatch?.board ?: "", "rnbqkbnr/pppppppp/8/5Q2/8/8/PPPPPPPP/RNB1KBNR b KQkq - 0 1")
		matchService.modify(nextStep)
		val modifeid = matchService.getMatchByID(match.matchId ?: -1)
		assert((modifeid?.board ?: " ") != "rnbqkbnr/pppppppp/8/5Q2/8/8/PPPPPPPP/RNB1KBNR b KQkq - 0 1" && (modifeid?.board ?: " ") == nextStep.prevBoard)
	}

	@Test()
	fun invalidPreviousBoard() {
		val savedMatch = matchService.getMatchByID(match.matchId ?: -1)
		val nextStep = StepRequest(savedMatch?.matchId ?: -1, savedMatch?.board ?: "", "NOT A TABLE")
		matchService.modify(nextStep)
		val modifeid = matchService.getMatchByID(match.matchId ?: -1)
		assert((modifeid?.board ?: " ") != "NOT A TABLE" && (modifeid?.board ?: " ") == nextStep.prevBoard)
	}

	@Test
	fun deleteMatch() {
		val savedMatch = matchService.getMatchByID(match2.matchId ?: -1)
		assert(savedMatch?.toString() == match2.toString())
		assert(matchService.findAll()?.contains(match2) ?: false)

		matchService.delete(match2)

		assert(matchService.findAll()?.contains(match2)?.not() ?: false)
	}

	@Test
	fun getMatchesByUser(){
		assert(matchService.getMatchesByUserID(user).size == 2)
		assert(matchService.getMatchesByUserID(user2).size == 1)
		assert(matchService.getMatchesByUserID(user3).filter { it.players.contains(user3) }.size == 1)
	}

	@Test
	fun deleteUser() {
		assert(userService.findAll().contains(user))
		assert(matchService.getMatchesByUserID(user).filter { it.players.contains(user) }.size == 2)

		userService.deleteUser(user.getId() ?: -1)

		assert(matchService.getMatchesByUserID(user).filter { it.players.contains(user) }.isEmpty())
		assert(userService.findAll().filter { it.toString() == user.toString() }.isEmpty())
	}

	@Test
	fun messageTest() {

		val message = Message("szia", user, user2)
		messageService.save(message)
		val saved = messageService.getMessageByID(message.messageId ?: -1)

		assert(saved == message)
		assert( messageService.getMessageByUser(user.getId() ?: -1, user2.getId() ?: -1)[0] == message)
	}

}
