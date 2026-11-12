package com.esportportal.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "favorite_teams")
class FavoriteTeamEntity {

    @EmbeddedId
    var id: FavoriteTeamId = FavoriteTeamId()

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id", nullable = false)
    var team: TeamEntity? = null
}