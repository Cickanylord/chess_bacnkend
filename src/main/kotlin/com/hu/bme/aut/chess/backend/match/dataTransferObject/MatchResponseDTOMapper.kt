package com.hu.bme.aut.chess.backend.match.dataTransferObject

import com.hu.bme.aut.chess.backend.match.Match
import org.springframework.stereotype.Service
import java.util.function.Function
@Service
class MatchResponseDTOMapper: Function<Match, MatchResponseDTO> {
    override fun apply(match: Match): MatchResponseDTO {
        return  MatchResponseDTO(
            match.getMatchId()!!,
            match.getChallenger().getId()!!,
            match.getChallenged().getId()!!,
            match.getBoard()
        )
    }
}