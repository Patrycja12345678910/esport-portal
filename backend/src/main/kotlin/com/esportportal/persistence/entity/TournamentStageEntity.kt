package com.esportportal.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name="tournament_stages")

class TournamentStageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    var tournament: TournamentEntity? = null

    @Column(nullable = false, length = 255)
    var name: String = ""

    @Column(name = "stage_type", length = 50)
    var stageType: String? = null

    @Column(name = "external_id", length = 100, unique = true)
    var externalId: String? = null
}