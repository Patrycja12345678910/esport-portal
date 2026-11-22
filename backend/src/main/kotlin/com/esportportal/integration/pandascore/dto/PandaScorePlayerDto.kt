package com.esportportal.integration.pandascore.dto

data class PandaScorePlayerDto(
    val id: Long,
    val name: String?,
    val first_name: String? = null,
    val last_name: String? = null,
    val nationality: String? = null,
    val current_team: PandaScorePlayerTeamDto? = null
)
