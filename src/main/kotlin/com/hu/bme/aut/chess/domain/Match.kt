package com.hu.bme.aut.chess.domain

import jakarta.persistence.*
import org.hibernate.annotations.Cascade


@Entity
class Match(

    @Id
    @GeneratedValue
    val matchId: Long? = null,


    @JoinTable(
        name = "players",
        joinColumns =  [JoinColumn( name = "match_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    @ManyToMany(fetch = FetchType.EAGER)
    val players: List<ChessUser>,

    var board: String


) {
    override fun toString(): String {
        super.toString()
        return "${matchId}_${players[0]}_${players[1]}_$board"
    }

    override fun equals(other: Any?): Boolean {
        if(other is Match? ){
            return other.toString() == this.toString()
        }
        return super.equals(other)
    }

    fun getMatchId():Long{
        return matchId ?: -1
    }


}


