package com.hu.bme.aut.chess.controller.ai

import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.Board
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/chess/ai")
class AiController {

    @GetMapping("/{fen}")
    fun getAiStep(@PathVariable fen: String): ResponseEntity<String>{
        val validFen = fenRequestToString(fen)
        println(validFen)
        val board = Board(validFen)
        board.doAiStep(PieceColor.WHITE)
        return ResponseEntity.ok("Next Step: ${board.createFEN()}\nprevious step: $fen")
        TODO("Implement ai")
    }

    fun fenRequestToString(fenReq: String): String {
        return fenReq.replace('.','/').replace("_", " ")
    }
}