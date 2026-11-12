package com.esportportal.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name="match_maps")

class MatchMapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    var match: MatchEntity? = null

    @Column(name = "map_name", length = 100)
    var mapName: String? = null

    @Column(name = "map_number")
    var mapNumber: Int? = null

    @Column(name = "home_score")
    var homeScore: Int? = null

    @Column(name = "away_score")
    var awayScore: Int? = null
}