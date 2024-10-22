package com.hu.bme.aut.chess.backend.games.chess.ai

import ai_engine.ai.ChessAI
import ai_engine.board.BoardData
import ai_engine.board.BoardLogic
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/chess/ai")
class AiController {

    @PostMapping("/fen")
    fun getAiStep(@RequestBody fenDTO: FenDTO): ResponseEntity<FenDTO>{
        val board = BoardLogic(BoardData(fenDTO.fen))

        val ai = ChessAI(board.board.activeColor, board.board)
        val nextStep = ai.getTheNextStep()
        board.move(nextStep.first, nextStep.second)

        println(board.board.toString())
        return ResponseEntity.ok(FenDTO(board.board.toString()))
        TODO("Implement ai")


    }

    data class FenDTO(val fen: String)

    fun fenRequestToString(fenReq: String): String {
        return fenReq.replace('%','/').replace("_", " ")
    }
}