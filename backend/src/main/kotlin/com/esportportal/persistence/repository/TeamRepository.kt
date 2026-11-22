package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TeamRepository : JpaRepository<TeamEntity, Long>
{
    fun findByExternalId(externalId: String): TeamEntity?

    @Query(
        value = """
        SELECT DISTINCT t.*
        FROM teams t
        JOIN matches m
            ON t.id = m.home_team_id OR t.id = m.away_team_id
        JOIN tournaments tr ON m.tournament_id = tr.id
        JOIN games g ON tr.game_id = g.id
        WHERE g.slug = :gameSlug
    """,
        nativeQuery = true
    )
    fun findByGameSlug(@Param("gameSlug") gameSlug: String): List<TeamEntity>
}
