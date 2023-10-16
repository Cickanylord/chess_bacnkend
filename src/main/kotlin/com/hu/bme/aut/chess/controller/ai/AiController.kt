package com.hu.bme.aut.chess.controller.ai

import com.hu.bme.aut.chess.ai_engine.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.Board
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chess/ai")
class AiController {

    @GetMapping("/{fen}")
    fun getAiStep(@PathVariable fen: String): ResponseEntity<String>{
        val board = Board(fenBoard= "rnbqkbnr/pppp1ppp/4p3/8/8/4P3/PPPP1PPP/RNBQKBNRK w KQkq")
        board.doAiStep(PieceColor.WHITE)
        return ResponseEntity.ok("Next Step: ${board.createFEN()}\nprevious step: $fen")
        TODO("Implement ai")
    }
}