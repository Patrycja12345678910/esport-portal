package com.esportportal.persistence.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "sync_import_log")
class SyncImportLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "source", nullable = false, length = 50)
    var source: String = ""

    @Column(name = "started_at", nullable = false)
    var startedAt: Instant = Instant.now()

    @Column(name = "finished_at")
    var finishedAt: Instant? = null

    @Column(name = "status", nullable = false, length = 20)
    var status: String =  ""

    @Column(name = "fetched_count")
    var fetchedCount: Int = 0

    @Column(name = "matched_count")
    var matchedCount: Int = 0

    @Column(name = "rejected_count")
    var rejectedCount: Int = 0

    @Column(name = "error_message")
    var errorMessage: String? = null
}