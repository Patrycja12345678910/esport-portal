package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.MatchEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import java.time.LocalDateTime

interface MatchRepository : JpaRepository<MatchEntity, Long>
{
    fun findByExternalId(externalId: String): MatchEntity?

    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<MatchEntity>

    fun findByIdAndDeletedAtIsNull(id: Long): MatchEntity?

    fun findAllByDeletedAtIsNullAndStatus(status: String, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentGameSlug(game: String, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentGameSlugAndStatus(game: String, status: String, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentId(tournamentId: Long, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentGameSlugAndStatusAndTournamentId(game: String, status: String, tournamentId: Long, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentGameSlugAndTournamentId(game: String,tournamentId: Long, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndStatusAndTournamentId(status: String, tournamentId: Long, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(start: LocalDateTime, end: LocalDateTime, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentGameSlugAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(game: String, start: LocalDateTime, end: LocalDateTime, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndStatusAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(status: String, start: LocalDateTime, end: LocalDateTime,pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentIdAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(tournamentId: Long, start: LocalDateTime, end: LocalDateTime, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentGameSlugAndStatusAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(game: String,status: String, start: LocalDateTime, end: LocalDateTime,pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentGameSlugAndTournamentIdAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(game: String, tournamentId: Long, start: LocalDateTime,end: LocalDateTime,pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndStatusAndTournamentIdAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(status: String, tournamentId: Long, start: LocalDateTime, end: LocalDateTime, pageable: Pageable): Page<MatchEntity>

    fun findByDeletedAtIsNullAndTournamentGameSlugAndStatusAndTournamentIdAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(game: String, status: String, tournamentId: Long, start: LocalDateTime,end: LocalDateTime,pageable: Pageable): Page<MatchEntity>

}

