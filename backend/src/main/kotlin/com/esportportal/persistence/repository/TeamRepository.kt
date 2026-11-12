package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<TeamEntity, Long>
{
    fun findByExternalId(externalId: String): TeamEntity?
}
