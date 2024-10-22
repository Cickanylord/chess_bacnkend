package com.hu.bme.aut.chess.backend.match

import com.hu.bme.aut.chess.backend.match.dataTransferObject.MatchRequestDTO
import com.hu.bme.aut.chess.backend.match.dataTransferObject.MatchResponseDTO
import com.hu.bme.aut.chess.backend.match.dataTransferObject.MatchResponseDTOMapper
import com.hu.bme.aut.chess.backend.match.dataTransferObject.StepRequest
import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserResponseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/matches")
abstract class MatchController @Autowired constructor(
    private val matchService: MatchService,
){
    @GetMapping
    fun getMatches(): ResponseEntity<List<MatchResponseDTO>> =
        ResponseEntity
            .ok(matchService.findAllMatches()
                .map { it.toMatchResponseDTO()!! }
            )


    @GetMapping("/{id}")
    fun getMatch(@PathVariable id: Long): ResponseEntity<MatchResponseDTO> =
        matchService
            .findMatchById(id)
            .toMatchResponseEntity()

    @GetMapping("/getMatchesWithPartner/{partnerId}")
    fun getMatchesWithPartner(@PathVariable partnerId: Long): ResponseEntity<List<MatchResponseDTO>> =
        ResponseEntity
            .ok(
                matchService
                .finedMatchesBetweenTwoPlayers(partnerId)
                .map { it.toMatchResponseDTO()!! }
            )

    @PostMapping
    fun addMatch(@RequestBody matchReq: MatchRequestDTO): ResponseEntity<MatchResponseDTO> =
    matchService
        .saveMatch(matchReq)
        .toMatchResponseEntity()
    
    @PutMapping("/step")
    fun step(@RequestBody step: StepRequest): ResponseEntity<MatchResponseDTO> =
        matchService
            .updateMatch(step)
            .toMatchResponseEntity()



    @DeleteMapping("/{id}")
    fun deleteMatch(@PathVariable id: Long): ResponseEntity<String> {
        matchService.deleteMatch(id)
        return ResponseEntity.ok("Match Deleted")
    }
}

fun Match?.toMatchResponseDTO(): MatchResponseDTO? {
    return this?.let { match ->
        MatchResponseDTO(
            match.getMatchId()!!,
            match.getChallenger().getId()!!,
            match.getChallenged().getId()!!,
            match.getBoard()
        )
    }
}

fun Match?.toMatchResponseEntity(): ResponseEntity<MatchResponseDTO>  {
    return this.toMatchResponseDTO()?.let {
        ResponseEntity.ok(it)
    } ?: ResponseEntity.notFound().build()
}