package com.esportportal.integration

import org.springframework.stereotype.Component

@Component
class TeamNameNormalizer {
    fun normalizujNazwe(teamName: String): String {
        return teamName.lowercase()
            .replace(" ", "")
            .replace("-", "")
            .replace(".", "")
    }

    fun normalizujRozszerzonaNazwe(teamName: String): String {
        val nazwa = teamName.lowercase().trim()
            .removePrefix("team")
            .removeSuffix(" team")
            .removeSuffix(" esports")

        return normalizujNazwe(nazwa)
    }

}