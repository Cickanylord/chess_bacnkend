package com.hu.bme.aut.chess.backend.match

import com.hu.bme.aut.chess.backend.match.dataTransferObject.MatchRequestDTO
import com.hu.bme.aut.chess.backend.match.dataTransferObject.MatchResponseDTO
import com.hu.bme.aut.chess.backend.match.dataTransferObject.MatchResponseDTOMapper
import com.hu.bme.aut.chess.backend.match.dataTransferObject.StepRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/matches")
abstract class MatchController @Autowired constructor(
    private val matchService: MatchService,
    private val matchResponseDTOMapper: MatchResponseDTOMapper
){
    @GetMapping
    fun getMatches(): ResponseEntity<List<MatchResponseDTO>> {
        return  ResponseEntity.ok(matchService.findAllMatches().map { matchResponseDTOMapper.apply(it) })
    }

    @GetMapping("/{id}")
    fun getMatch(@PathVariable id: Long): ResponseEntity<MatchResponseDTO> {
        return matchService.findMatchById(id)?.let {
            ResponseEntity.ok(matchResponseDTOMapper.apply(it))
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun addMatch(@RequestBody matchReq: MatchRequestDTO): ResponseEntity<MatchResponseDTO> {
        return matchService.saveMatch(matchReq)?.let {
            ResponseEntity.ok(matchResponseDTOMapper.apply(it))
        } ?: ResponseEntity.notFound().build()
    }
    
    @PutMapping("/step")
    fun step(@RequestBody step: StepRequest): ResponseEntity<MatchResponseDTO> {
        return matchService.updateMatch(step)?.let {
            ResponseEntity.ok(matchResponseDTOMapper.apply(it))
        } ?: ResponseEntity.notFound().build()
    }


    @DeleteMapping("/{id}")
    fun deleteMatch(@PathVariable id: Long): ResponseEntity<String> {
        matchService.deleteMatch(id)
        return ResponseEntity.ok("Match Deleted")
    }
}