package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.TournamentStageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TournamentStageRepository : JpaRepository<TournamentStageEntity, Long> {
    fun findByExternalId(externalId: String): TournamentStageEntity?
}

