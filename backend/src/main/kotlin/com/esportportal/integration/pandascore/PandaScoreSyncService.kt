package com.esportportal.integration.pandascore

import com.esportportal.persistence.entity.TeamEntity
import com.esportportal.persistence.repository.TeamRepository
import com.esportportal.persistence.repository.TournamentRepository
import com.esportportal.persistence.entity.TournamentEntity
import com.esportportal.persistence.entity.GameEntity
import com.esportportal.persistence.repository.GameRepository
import org.springframework.stereotype.Service

@Service
class PandaScoreSyncService(
    private val teamRepository: TeamRepository,
    private val tournamentRepository: TournamentRepository,
    private val gameRepository: GameRepository,
    private val pandaScoreClient: PandaScoreClient
) {
    fun findTeam(externalId: String): TeamEntity? {
        return teamRepository.findByExternalId(externalId)
    }

    fun findGame(slug: String): GameEntity? {
        return gameRepository.findBySlug(slug)
    }


    fun saveTeam(externalId: String, name: String): TeamEntity {
        val team = teamRepository.findByExternalId(externalId)
            ?: TeamEntity()

        team.externalId = externalId
        team.name = name

        return teamRepository.save(team)
    }

    fun saveTournament(externalId: String, name: String, game: GameEntity): TournamentEntity {
        val tournament = tournamentRepository.findByExternalId(externalId)
            ?: TournamentEntity()

        tournament.externalId = externalId
        tournament.name = name
        tournament.game = game

        return tournamentRepository.save(tournament)
    }

    fun getCs2Game(): GameEntity {
        val game = gameRepository.findBySlug("cs2")
            ?: error("Nie znaleziono gry CS2")

        return game
    }

    fun syncMatches(){
        val matches = pandaScoreClient.pobierzNadchodzaceMeczeCS2()

        val game = getCs2Game()

        for (match in matches) {
            println(match.name)

            val league = match.league
            val serie = match.serie
            val serieId = serie?.id ?: continue

            val leagueName = league?.name?.takeIf { it.isNotBlank() } ?: "Nieznana liga"

            val tournamentName = if(serie?.name.isNullOrBlank()) {
                leagueName
            } else {
                "$leagueName ${serie?.name}"
            }

            println(league?.name)
            println(serie?.name)

            saveTournament(serieId.toString(), tournamentName, game)

            val opponents = match.opponents ?: emptyList()

            for (opponent in opponents) {
                val team = opponent.opponent?: continue
                val name = team.name ?: continue


                println(team.name)
                println(team.id)

                saveTeam(team.id.toString(), name)


            }
        }
    }
}
