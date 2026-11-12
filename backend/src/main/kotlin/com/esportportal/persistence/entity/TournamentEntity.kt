package com.esportportal.persistence.entity
import java.time.LocalDateTime

import jakarta.persistence.*

@Entity
@Table(name="tournaments")

class TournamentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    var game: GameEntity? = null

    @Column(nullable = false, length = 255)
    var name: String = ""

    @Column(name = "start_date")
    var startDate: LocalDateTime? = null

    @Column (name = "end_date")
    var endDate: LocalDateTime? = null

    @Column(length = 50)
    var status:String?= null

    @Column(name = "external_id", length = 100)
    var externalId:String?= null

}