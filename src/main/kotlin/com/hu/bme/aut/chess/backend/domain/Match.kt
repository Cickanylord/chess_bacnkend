package com.hu.bme.aut.chess.backend.domain

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter


@Entity
@Setter
@Getter
class Match(
        @Id
        @GeneratedValue
        val matchId: Long? = null)

        /*
            //TODO make this atomic
                @JoinTable(
                name = "players",
                joinColumns =  [JoinColumn( name = "match_id")],
                inverseJoinColumns = [JoinColumn(name = "user_id")]
            )
            @ManyToMany(fetch = FetchType.LAZY)
            val players: List<User>,

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


         */




