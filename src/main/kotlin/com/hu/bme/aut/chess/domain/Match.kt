package com.hu.bme.aut.chess.domain

import jakarta.persistence.*
import org.hibernate.annotations.Cascade

@Entity
class Match(
    @Id
    @GeneratedValue
    val matchId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "player_one_id")
    val playerOne: ChessUser,

    @ManyToOne
    @JoinColumn(name = "player_two_id", )
    val playerTwo: ChessUser,

    var board: String
)


