package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.ApiRequestLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
interface ApiRequestLogRepository : JpaRepository<ApiRequestLogEntity, Long> {

    fun countBySourceAndRequestedAtAfter(
        source: String,
        odKiedy: Instant
    ): Long


}