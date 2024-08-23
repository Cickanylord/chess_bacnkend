package com.hu.bme.aut.chess.backend.repository.match

import com.hu.bme.aut.chess.backend.domain.Match
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MatchRepository : JpaRepository<Match, Long>

