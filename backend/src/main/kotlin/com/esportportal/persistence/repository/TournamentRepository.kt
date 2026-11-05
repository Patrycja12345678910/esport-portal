package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.TournamentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TournamentRepository : JpaRepository<TournamentEntity, Long>
{
    fun findByExternalId(externalId: String): TournamentEntity?
}
