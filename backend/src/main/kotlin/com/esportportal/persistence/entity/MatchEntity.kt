package com.esportportal.persistence.entity
import java.time.LocalDateTime
import java.time.Instant
import jakarta.persistence.*

@Entity
@Table(name="matches")

class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    var tournament: TournamentEntity? = null

    @ManyToOne
    @JoinColumn(name = "stage_id")
    var stage: TournamentStageEntity? = null

    @ManyToOne
    @JoinColumn(name = "home_team_id", nullable = true)
        var homeTeam: TeamEntity? = null

    @ManyToOne
    @JoinColumn(name = "away_team_id", nullable = true)
    var awayTeam: TeamEntity? = null

    @Column(name = "scheduled_at")
    var scheduledAt: LocalDateTime? = null

    @Column(length = 50)
    var status: String? = null

    @Column(name = "home_score")
    var homeScore: Int? = null

    @Column(name = "away_score")
    var awayScore: Int? = null

    @Column(name = "external_id", length = 100)
    var externalId: String? = null

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null
}