package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.MatchEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MatchRepository : JpaRepository<MatchEntity, Long>
{
    fun findByExternalId(externalId: String): MatchEntity?
}

