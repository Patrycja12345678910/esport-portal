package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.SyncStateEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SyncStateRepository: JpaRepository<SyncStateEntity, Long> {
    fun findBySource(source: String): SyncStateEntity?
}