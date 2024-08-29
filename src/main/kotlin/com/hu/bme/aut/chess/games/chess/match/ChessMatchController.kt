package com.hu.bme.aut.chess.games.chess.match

import com.hu.bme.aut.chess.backend.match.dataTransferObject.MatchResponseDTOMapper
import com.hu.bme.aut.chess.backend.match.MatchController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/chessMatch")
class ChessMatchController @Autowired constructor(
    private val chessMatchService: ChessMatchService,
) : MatchController(chessMatchService)