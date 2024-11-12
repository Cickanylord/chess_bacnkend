package com.hu.bme.aut.chess.games.chess.match

import ai_engine.board.BoardData
import ai_engine.board.BoardLogic
import ai_engine.board.pieces.enums.PieceColor
import com.hu.bme.aut.chess.backend.match.Match
import com.hu.bme.aut.chess.backend.match.MatchRepository
import com.hu.bme.aut.chess.backend.match.MatchService
import com.hu.bme.aut.chess.backend.match.dataTransferObject.StepRequest
import com.hu.bme.aut.chess.backend.users.User
import com.hu.bme.aut.chess.backend.users.UserService
import com.hu.bme.aut.chess.backend.webSocket.MatchEndPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChessMatchService @Autowired constructor(
    private val matchRepository: MatchRepository,
    private val userService: UserService,
    private val matchEndPoint: MatchEndPoint
) : MatchService(matchRepository, userService) {

    /**
     * This function is responsible for updating the chess games.
     * if the request is sent by other than participant of the match the request should be disregarded.
     * if the request contains an invalid board it shall be ignored and send the last consistent state of the board to the requester
     * if the request is good then it shall be checked if the step finishes the game.
     * if the step finishes handle game finishing.
     * return the next state of the board and notify the participants of the updated trough websockets
     *
     * @param step the next step
     * @return the state of the match
     */
    override fun updateMatch(step: StepRequest): Match? {
        val match = findMatchById(step.matchId) ?: return null
        val authenticatedUser = userService.findAuthenticatedUser() ?: return null

        // Check if the authenticated user is a participant in the match
        if (authenticatedUser == match.getChallenged() || authenticatedUser == match.getChallenger()) {
            val prevBoard = BoardData(match.getBoard())
            val possibleFens = prevBoard.boardLogic.getPossibleFENs()
            // Verify if the new board state is valid
            if (isMoveValid(match, authenticatedUser, prevBoard, step)) {
                // Update the match's board to the new state
                match.setBoard(step.board)
                val nextBoard = BoardData(step.board)

                // Check if the game has reached a finishing state (no possible moves)
                if (nextBoard.boardLogic.getPossibleFENs().isEmpty()) {
                    // Determine the winner based on the active color
                    val winner = if (nextBoard.activeColor == PieceColor.BLACK) {
                        match.getChallenger()
                    } else {
                        match.getChallenged()
                    }
                    val loser =
                        if (winner == match.getChallenger()) match.getChallenged() else match.getChallenger()
                    match.finishMatch(winner, loser)
                }

                // Save the updated match and notify both players
                val savedMatch = matchRepository.save(match)
                matchEndPoint.sendMessage(
                    savedMatch,
                    listOf(savedMatch.getChallenged().getId(), savedMatch.getChallenger().getId())
                )
                return savedMatch
            } else {
                // If the new board state is invalid, return the previous match state
                return match
            }
        }
        return null
    }

    private fun isMoveValid(match: Match, user: User, board: BoardData, step: StepRequest): Boolean {
        val possibleFens = board.boardLogic.getPossibleFENs()
        val isUserTurn = (user == match.getChallenger() && board.activeColor == PieceColor.WHITE) ||
                (user == match.getChallenged() && board.activeColor == PieceColor.BLACK)
        return isUserTurn && possibleFens.contains(step.board)
    }

    override fun finedMatchesBetweenTwoPlayers(partnerId: Long): List<Match> {
        val user = userService.findAuthenticatedUser() ?: return listOf()
        return (user.getChallenger() + user.getChallenged())
            .filter { match ->
                match.getChallenged().getId() == partnerId ||
                match.getChallenger().getId() == partnerId
            }
    }

    fun BoardLogic.getPossibleFENs(): List<String> {
        val fenList = mutableListOf<String>()
        val pieces = board.getPiecesByColor(board.activeColor)

        for (piece in pieces) {
            val legalMoves = getLegalMoves(piece)

            for (move in legalMoves) {
                val tmpBoard = BoardLogic(BoardData(board.toString()))

                tmpBoard.move(tmpBoard.board.getPiece(piece.position), move)
                fenList.add(tmpBoard.board.toString())
            }
        }
        return fenList
    }
}