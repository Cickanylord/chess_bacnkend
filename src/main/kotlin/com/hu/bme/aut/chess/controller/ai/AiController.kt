package com.hu.bme.aut.chess.controller.ai

import com.hu.bme.aut.chess.ai_engine.board.BoardData
import com.hu.bme.aut.chess.ai_engine.board.BoardLogic
import com.hu.bme.aut.chess.ai_engine.ai.NewAI
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
        //TODO("make fen valid")

        val board = BoardLogic(BoardData(validFen))

        val ai = NewAI(board.board.activeColor ,board.board)
        val nextStep = ai.getTheNextStep()
        board.move(nextStep.first, nextStep.second)

        return ResponseEntity.ok("Next Step: ${board.board.fen}\nprevious step: $fen")

        /*
        val board = Board(validFen)
        board.doAiStep(PieceColor.WHITE)
        return ResponseEntity.ok("Next Step: ${board.createFEN()}\nprevious step: $fen")
        TODO("Implement ai")

         */
    }

    fun fenRequestToString(fenReq: String): String {
        return fenReq.replace('.','/').replace("_", " ")
    }
}