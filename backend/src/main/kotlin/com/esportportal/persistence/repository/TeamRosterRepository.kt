package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.TeamRosterEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRosterRepository: JpaRepository<TeamRosterEntity, Long> {

    fun findByTeam_IdAndPlayer_IdAndLeftAtIsNull(
        teamId: Long, playerId: Long
    ): TeamRosterEntity?

    fun findByPlayer_IdAndLeftAtIsNull(
        playerId: Long
    ): List<TeamRosterEntity>
}