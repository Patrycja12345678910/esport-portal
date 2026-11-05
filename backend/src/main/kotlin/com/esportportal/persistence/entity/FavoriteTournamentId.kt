package com.esportportal.persistence.entity

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class FavoriteTournamentId(

    @Column(name = "user_id", nullable = false)
    var userId: Long = 0,

    @Column(name = "tournament_id", nullable = false)
    var tournamentId: Long = 0

) : Serializable