package com.hu.bme.aut.chess.backend.match

import com.hu.bme.aut.chess.backend.users.User
import jakarta.persistence.*
import lombok.Getter
import lombok.Setter


@Entity
class Match(
        @Id
        @GeneratedValue
        private val matchId: Long? = null,

        @ManyToOne
        private val challenger: User,

        @ManyToOne
        private val challenged: User,
) {
        private var board: String = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

        fun getMatchId(): Long? = matchId

        fun getChallenger(): User = challenger

        fun getChallenged(): User = challenged

        fun getBoard(): String = board

        fun setBoard(board: String) {
                this.board = board
        }

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




