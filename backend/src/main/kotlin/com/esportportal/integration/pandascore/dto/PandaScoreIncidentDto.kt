package com.esportportal.integration.pandascore.dto

import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PandaScoreIncidentDto(
    val id: Long,
    val type: String,
    val changeType: String? = null,
    val modifiedAt: String? = null,
    val `object`: PandaScoreMatchDto? = null
)