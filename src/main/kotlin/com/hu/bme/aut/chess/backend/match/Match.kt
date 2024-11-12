package com.hu.bme.aut.chess.backend.match

import com.hu.bme.aut.chess.backend.users.User
import jakarta.persistence.*

@Entity
class Match(
        @Id
        @GeneratedValue
        private val matchId: Long? = null,

        @ManyToOne
        private val challenger: User,

        @ManyToOne
        private val challenged: User,

        private var board: String
) {
        /**
         * this field shows if the match is finished or not
         */
        private var isGoing: Boolean = true

        @ManyToOne
        private var winner: User? = null

        @ManyToOne
        private var loser : User? = null

        fun getMatchId(): Long? = matchId

        fun getChallenger(): User = challenger

        fun getChallenged(): User = challenged

        fun getBoard(): String = board

        fun setBoard(board: String) {
                this.board = board
        }

        fun getIsGoing(): Boolean = isGoing

        /**
         * This function finishes the match
         * @param winner the winner of the match
         * @param loser the loser of the match
         */
        fun finishMatch(winner: User, loser: User) {
                isGoing = false
                this.winner = winner
                this.loser = loser
        }

        fun getPlayers(): List<User> = listOf(challenged, challenger)

        fun getWinner() = winner
        fun getLoser() = loser

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Match

                return matchId == other.matchId
        }

        override fun hashCode(): Int {
                return matchId?.hashCode() ?: 0
        }

        override fun toString(): String {
                return "Match(matchId=$matchId, challenger=$challenger, challenged=$challenged, board='$board')"
        }

}




