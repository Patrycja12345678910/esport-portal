package com.esportportal.persistence.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name="player_match_statistics")

class PlayerMatchStatisticsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    var match: MatchEntity? = null

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    var player: PlayerEntity? = null

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    var team: TeamEntity? = null

    @Column(name = "kills")
    var kills: Int? = null

    @Column(name = "deaths")
    var deaths: Int? = null

    @Column(name = "assists")
    var assists: Int? = null

    @Column(name = "rating")
    var rating: BigDecimal? = null


}