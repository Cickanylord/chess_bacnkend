package com.hu.bme.aut.chess.repository.match

import com.hu.bme.aut.chess.domain.ChessUser
import com.hu.bme.aut.chess.domain.Match
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MatchRepository : JpaRepository<Match, Long>

