package com.esportportal.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "favorite_tournaments")
class FavoriteTournamentEntity {

    @EmbeddedId
    var id: FavoriteTournamentId = FavoriteTournamentId()

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null

    @ManyToOne
    @MapsId("tournamentId")
    @JoinColumn(name = "tournament_id", nullable = false)
    var tournament: TournamentEntity? = null
}