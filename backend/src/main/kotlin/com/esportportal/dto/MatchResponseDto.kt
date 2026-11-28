package com.esportportal.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class MatchResponseDto(
    val id: Long,
    val tournamentName: String?,
    val homeTeamName: String?,
    val awayTeamName: String?,
    val scheduledAt: LocalDateTime?,
    val status: String?,
    val homeScore: Int?,
    val awayScore: Int?,
    val gameSlug: String?,
    val tournamentId: Long?,
)
