package com.esportportal.integration.pandascore.dto

import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PandaScoreResultDto(
    val teamId: Long? = null,
    val score: Int? = null
)
