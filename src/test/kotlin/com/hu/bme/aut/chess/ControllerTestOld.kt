package com.hu.bme.aut.chess



import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerTestOld @Autowired constructor(
        val client: TestRestTemplate,

){
    /*
    val listOfUser  = listOf(User("Bob"), User("Alice"))

    @LocalServerPort
    var randomServerPort = 0

    @Test
    fun userPost() {
        val uri = URI("http://localhost:$randomServerPort/api/users/addUser")

        val listOfRequest: List<HttpEntity<User>> = listOfUser.map {
            HttpEntity<User>(it, HttpHeaders())
        }

        val results = listOfRequest.map { client.postForEntity(uri, it, String::class.java) }


        val control = client.getForEntity<List<User>>(URI("http://localhost:$randomServerPort/api/users"))

        val userList: List<User> = fromGsonToList(control.body)

        results.forEach {
            assert(it.statusCode == HttpStatus.OK)
        }

        assert(control.statusCode == HttpStatus.OK)
        assert(userService.findAll().containsAll(userList))


    }

    @Test
    fun matchPost() {
        listOfUser.forEach { userService.save(it) }
        val matchRequest = MatchRequestDTO(listOfUser[0].getId() ?: -1, listOfUser[1].getId() ?: -1, "")

        val uri = URI("http://localhost:$randomServerPort/api/matches/addMatch")
        val request: HttpEntity<MatchRequestDTO> = HttpEntity<MatchRequestDTO>(matchRequest, HttpHeaders())
        val postResult: ResponseEntity<Match> = client.postForEntity(uri, request)

        assert(postResult.statusCode == HttpStatus.OK)
        assert(matchService.getMatchByID(postResult.body?.getMatchId()!!) == postResult.body)

    }
    @Test
    fun matchValidStep() {
        listOfUser.forEach { userService.save(it) }

        val match = Match(players = listOfUser, board = "")
        matchService.save(match)
        val uri = URI("http://localhost:$randomServerPort/api/matches/step")


        val stepRequest = StepRequest(
            match.getMatchId(),
            matchService.getMatchByID(match.matchId ?: -1)?.board ?: " ",
            "rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1")
        val postResult: ResponseEntity<Match> = client.postForEntity(uri, stepRequest)

        assert(postResult.statusCode == HttpStatus.OK)
        assert(matchService.getMatchByID(match.getMatchId()) == postResult.body)
    }

    @Test
    fun matchInvalidStep() {
        listOfUser.forEach { userService.save(it) }
        val match = Match(players = listOfUser, board = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
        matchService.save(match)

        val uri = URI("http://localhost:$randomServerPort/api/matches/step")

        val stepRequest = StepRequest(
            match.getMatchId(),
            matchService.getMatchByID(match.matchId ?: -1)?.board ?: " ",
            "NOT A VALID BOARD")

        val postResult: ResponseEntity<Match> = client.postForEntity(uri, stepRequest)

        assert(postResult.statusCode == HttpStatus.NOT_FOUND)
        assert(matchService.getMatchByID(match.getMatchId())?.board == "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    }
    @Test
    fun matchDelete() {
        listOfUser.forEach { userService.save(it) }
        val match = Match(players = listOfUser, board = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
        matchService.save(match)

        val delete = client.getForEntity<String>(URI("http://localhost:$randomServerPort/api/matches/deleteMatch/${match.matchId}"))
        assert(delete.statusCode == HttpStatus.OK)
        assert(matchService.getMatchByID(match.getMatchId()) == null)
    }

    @Test
    fun aiStep(){
        val nextStep = client.getForEntity<String>(URI("http://localhost:$randomServerPort/api/chess/ai/rnbqkbnr.pppp1ppp.4p3.8.8.4P3.PPPP1PPP.RNBQKBNR_w_KQkq"))
        assert(nextStep.statusCode == HttpStatus.OK)
    }

    @Test
    fun messagePostTest(){
        listOfUser.forEach { userService.save(it) }
        val uri = URI("http://localhost:$randomServerPort/api/messages/postMessage")

        val messageRequestDTO = MessageRequestDTO(listOfUser[0].getId() ?: -1, listOfUser[1].getId() ?: -1, "Hello World")
        val postResult: ResponseEntity<Message> = client.postForEntity(uri, messageRequestDTO)

        assert(postResult.statusCode == HttpStatus.OK)
        assert(messageService.findAll()?.contains(postResult.body) ?: false)
    }

    @Test
    fun messageGetMessagesByUserTest(){
        listOfUser.forEach { userService.save(it) }
        Message("Hello World", listOfUser[0], listOfUser[1]).let {
            messageService.save(it)
            println("it")
            val result = client.getForEntity<List<Message>>(URI("http://localhost:$randomServerPort/api/messages/getMessage/${listOfUser[0].getId()}/${listOfUser[1].getId()}"))

            val messageList:  List<Message> = fromGsonToList(result.body)
            assert(result.statusCode == HttpStatus.OK)
            assert(messageList.contains(it))

            val control = client.getForEntity<List<User>>(URI("http://localhost:$randomServerPort/api/users"))


        }
    }

     */

}