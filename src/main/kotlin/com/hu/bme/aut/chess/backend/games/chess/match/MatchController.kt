package com.hu.bme.aut.chess.backend.games.chess.match

import com.hu.bme.aut.chess.backend.games.chess.match.DTO.MatchRequestDTO
import com.hu.bme.aut.chess.backend.games.chess.match.DTO.MatchResponseDTO
import com.hu.bme.aut.chess.backend.games.chess.match.DTO.MatchResponseDTOMapper
import com.hu.bme.aut.chess.backend.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/matches")
class MatchController @Autowired constructor(
    private val matchRepository: MatchRepository,
    private val userRepository: UserRepository,
    private val matchResponseDTOMapper: MatchResponseDTOMapper
){
    @GetMapping
    fun getMatches(): ResponseEntity<List<MatchResponseDTO>> {
        return  ResponseEntity.ok(matchRepository.findAll().map { matchResponseDTOMapper.apply(it) })
    }

    @PostMapping
    fun addMatch(@RequestBody matchReq: MatchRequestDTO): ResponseEntity<Match> {
        if(matchReq.challenged != matchReq.challenger) {
            val challenger = userRepository.findById(matchReq.challenger)
            val challenged = userRepository.findById(matchReq.challenged)
            when {
                challenger.isPresent && challenged.isPresent -> {
                    val match = Match(challenger = challenger.get(), challenged =  challenged.get())
                    matchRepository.save(match)
                    return ResponseEntity.ok(match)
                }
            }
        }
        return ResponseEntity.notFound().build()
    }
    
    @PostMapping("/step")
    fun Step(@RequestBody step: StepRequest): ResponseEntity<MatchResponseDTO> {
        return matchRepository.findById(step.match_id).map { match ->
            // TODO: verification of new board
            match.setBoard(step.board)
            val updatedMatch = matchRepository.save(match)
            ResponseEntity.ok(matchResponseDTOMapper.apply(updatedMatch))
        }.orElseGet {
            ResponseEntity.notFound().build()
        }
    }


    @DeleteMapping("/{match_id}")
    fun deleteMatch(@PathVariable match_id: Long): ResponseEntity<String> {
        matchRepository.deleteById(match_id)
        return ResponseEntity.ok("Match Deleted")
    }
}