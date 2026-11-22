package com.esportportal.persistence.entity

import jakarta.persistence.*
import java.time.Instant


@Entity
@Table(name = "api_request_log")
class ApiRequestLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "source", nullable = false, length = 50)
    var source: String = ""

    @Column(name = "requested_at", nullable = false)
    var requestedAt: Instant = Instant.now()
}