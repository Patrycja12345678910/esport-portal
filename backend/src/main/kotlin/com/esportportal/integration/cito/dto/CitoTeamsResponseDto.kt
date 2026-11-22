package com.esportportal.integration.cito.dto

data class CitoTeamsResponseDto (
    val success: Boolean,
    //lista pobranych drużyn
    val data: List<CitoTeamDto>,
    //metadane dot. paginacji
    val meta: CitoMetaDto
)
