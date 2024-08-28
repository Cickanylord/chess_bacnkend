package com.hu.bme.aut.chess.backend.games.chess.ai

import com.hu.bme.aut.chess.backend.match.DTO.MatchResponseDTOMapper
import com.hu.bme.aut.chess.backend.match.MatchController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/chessMatch")
class ChessMatchController @Autowired constructor(
    private val chessMatchService: ChessMatchService,
    private val matchResponseDTOMapper: MatchResponseDTOMapper
) : MatchController(chessMatchService, matchResponseDTOMapper) {

}