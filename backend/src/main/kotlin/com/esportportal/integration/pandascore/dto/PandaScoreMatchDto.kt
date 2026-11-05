package com.esportportal.integration.pandascore.dto

import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PandaScoreMatchDto(
    val id: Long,
    val name: String?,
    val status: String?,
    val scheduledAt: String?,
    val tournament: PandaScoreTournamentDto?,
    val opponents: List<PandaScoreOpponentDto>?,
    val league: PandaScoreLeagueDto?
)