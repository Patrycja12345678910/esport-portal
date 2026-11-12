package com.esportportal.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.*
import java.time.Instant


@Entity
@Table(name = "sync_state")
class SyncStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "source", nullable = false, unique = true, length = 100)
    var source: String? = null

    @Column(name = "last_successful_sync")
    var lastSuccessfulSync: Instant? = null
}