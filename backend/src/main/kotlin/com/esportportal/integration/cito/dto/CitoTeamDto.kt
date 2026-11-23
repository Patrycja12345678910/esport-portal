package com.esportportal.integration.cito.dto

data class CitoTeamDto (
    val id: String,
    val name: String,
    val slug: String? = null,
    val countryName: String? = null,
)
