package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.TeamExternalIdEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeamExternalIdRepository : JpaRepository<TeamExternalIdEntity, Long> {
    fun findBySourceAndExternalId( source: String,externalId: String): TeamExternalIdEntity?
}