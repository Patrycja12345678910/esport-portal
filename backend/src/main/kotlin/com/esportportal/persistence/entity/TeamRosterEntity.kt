package com.esportportal.persistence.entity
import java.time.LocalDateTime

import jakarta.persistence.*

@Entity
@Table(name="team_rosters")

class TeamRosterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "team_id", nullable=false)
    var team: TeamEntity? = null

    @ManyToOne
    @JoinColumn(name = "player_id", nullable=false)
    var player: PlayerEntity? = null

    @Column(name = "joined_at")
    var joinedAt: LocalDateTime?= null

    @Column(name = "left_at")
    var leftAt:LocalDateTime?=null
}