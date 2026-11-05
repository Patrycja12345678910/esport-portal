package com.esportportal.persistence.entity

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class FavoriteTeamId(

    @Column(name = "user_id", nullable = false)
    var userId: Long = 0,

    @Column(name = "team_id", nullable = false)
    var teamId: Long = 0

) : Serializable