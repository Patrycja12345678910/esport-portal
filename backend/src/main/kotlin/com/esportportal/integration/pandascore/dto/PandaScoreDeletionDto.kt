package com.esportportal.integration.pandascore.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PandaScoreDeletionDto(
    val id: Long,
    val type: String,
    val changeType: String,
    val modifiedAt: String,
    val `object`: PandaScoreDeletionDetailsDto
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PandaScoreDeletionDetailsDto(
    val deletedAt: String? = null,
    val reason: String? = null
)