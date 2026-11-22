package com.esportportal.integration.pandascore

import com.esportportal.integration.pandascore.dto.PandaScorePlayerDto
import com.esportportal.persistence.entity.PlayerEntity
import com.esportportal.persistence.entity.TeamEntity
import com.esportportal.persistence.entity.TeamRosterEntity
import com.esportportal.persistence.repository.PlayerRepository
import com.esportportal.persistence.repository.TeamRepository
import com.esportportal.persistence.repository.TeamRosterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class PandaScorePlayerService(
    private val playerRepository: PlayerRepository,
    private val teamRepository: TeamRepository,
    private val teamRosterRepository: TeamRosterRepository
    ) {

    fun savePlayer(playerDto: PandaScorePlayerDto): PlayerEntity {
        val player = playerRepository.findByExternalId(playerDto.id.toString())
            ?: PlayerEntity()

        player.externalId = playerDto.id.toString()
        player.nickname = playerDto.name?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Brak nicku zawodnika")
        player.firstName = playerDto.first_name
        player.lastName = playerDto.last_name
        player.nationality = playerDto.nationality


        return playerRepository.save(player)
    }

    fun savePlayerRoster(player: PlayerEntity, team: TeamEntity) {
        val poprzedniePrzypisania = teamRosterRepository.findByPlayer_IdAndLeftAtIsNull(player.id!!)

        poprzedniePrzypisania.forEach { przypisanie ->
            if (przypisanie.team?.id != team.id) {
                przypisanie.leftAt = LocalDateTime.now()
                teamRosterRepository.save(przypisanie)
            }
        }

        val aktualnePrzypisanie = teamRosterRepository.findByTeam_IdAndPlayer_IdAndLeftAtIsNull(
            team.id!!,
            player.id!!
        )

        if(aktualnePrzypisanie != null) {
            return
        }

        val nowePrzypisanie = TeamRosterEntity()
        nowePrzypisanie.player = player
        nowePrzypisanie.team = team
        nowePrzypisanie.joinedAt = null

        teamRosterRepository.save(nowePrzypisanie)

    }

    @Transactional
    fun zapiszZawodnika(zawodnik: PandaScorePlayerDto) {
        val zapisanyZawodnik = savePlayer(zawodnik)

        val teamId = zawodnik.current_team?.id
        if (teamId != null) {
            val druzyna = teamRepository.findByExternalId(teamId.toString())

            if (druzyna != null) {
                savePlayerRoster(zapisanyZawodnik, druzyna)
            }
        }
    }

    }

