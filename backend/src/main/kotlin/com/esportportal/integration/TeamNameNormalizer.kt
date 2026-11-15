package com.esportportal.integration

class TeamNameNormalizer {
    fun normalizujNazwe(teamName: String): String {
        return teamName.lowercase()
            .replace(" ", "")
            .replace("-", "")
            .replace(".", "")
    }

}