package com.esportportal.integration.pandascore

import com.esportportal.persistence.entity.TeamEntity
import com.esportportal.persistence.repository.TeamRepository
import com.esportportal.persistence.repository.TournamentRepository
import com.esportportal.persistence.entity.TournamentEntity
import com.esportportal.persistence.entity.GameEntity
import com.esportportal.persistence.entity.MatchEntity
import com.esportportal.persistence.entity.TournamentStageEntity
import com.esportportal.persistence.repository.GameRepository
import com.esportportal.persistence.repository.TournamentStageRepository
import com.esportportal.persistence.repository.MatchRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class PandaScoreSyncService(
    private val teamRepository: TeamRepository,
    private val tournamentRepository: TournamentRepository,
    private val gameRepository: GameRepository,
    private val pandaScoreClient: PandaScoreClient,
    private val tournamentStageRepository: TournamentStageRepository,
    private val matchRepository: MatchRepository
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

    fun saveStage(externalId: String, name: String, tournament: TournamentEntity): TournamentStageEntity {
        val tournamentStage = tournamentStageRepository.findByExternalId(externalId)
        ?: TournamentStageEntity()

        tournamentStage.externalId = externalId
        tournamentStage.name = name
        tournamentStage.tournament = tournament

        return tournamentStageRepository.save(tournamentStage)
    }

    fun saveMatch(externalId: String, tournament: TournamentEntity, stage: TournamentStageEntity?, homeTeam: TeamEntity, awayTeam: TeamEntity, scheduledAt: LocalDateTime?, status: String, homeScore: Int?, awayScore:Int?): MatchEntity {
        val match = matchRepository.findByExternalId(externalId)
            ?: MatchEntity()

        match.externalId = externalId
        match.tournament = tournament
        match.stage = stage
        match.homeTeam = homeTeam
        match.awayTeam = awayTeam
        match.scheduledAt = scheduledAt
        match.status = status
        match.awayScore = awayScore
        match.homeScore = homeScore

        return matchRepository.save(match)

    }

    fun getCs2Game(): GameEntity {
        val game = gameRepository.findBySlug("cs2")
            ?: error("Nie znaleziono gry CS2")

        return game
    }

    fun syncMatches(){
        val upcomingMatches = pandaScoreClient.pobierzNadchodzaceMeczeCS2()
        val pastMatches = pandaScoreClient.pobierzZakonczoneMeczeCS2()
        val matches = upcomingMatches + pastMatches

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


            val tournament = saveTournament(serieId.toString(), tournamentName, game)

            val stage = match.tournament
            val stageId = stage?.id ?: continue

            val stageName = stage.name ?: continue

            val savedStage = saveStage(stageId.toString(), stageName, tournament )

            val opponents = match.opponents ?: emptyList()
            val firstOpponent = opponents.getOrNull(0)
            val secondOpponent = opponents.getOrNull(1)
            val firstTeam = firstOpponent?.opponent ?: continue
            val secondTeam = secondOpponent?.opponent ?: continue
            val firstTeamName = firstTeam.name ?: continue
            val secondTeamName = secondTeam.name ?: continue

            val savedFirstTeam = saveTeam(firstTeam.id.toString(), firstTeamName)
            val savedSecondTeam = saveTeam(secondTeam.id.toString(), secondTeamName)

            val scheduledAtText = match.scheduledAt
            val scheduledAtInstant = scheduledAtText?.let { Instant.parse(it) }
            val scheduledAt = scheduledAtInstant?.let { LocalDateTime.ofInstant(it, ZoneOffset.UTC) }
            val matchStatus = match.status ?: "unknown"
            val firstTeamResult = match.results?.find {it.teamId == firstTeam.id}
            val secondTeamResult = match.results?.find {it.teamId == secondTeam.id}
            val homeScore = if (matchStatus == "finished") {
                firstTeamResult?.score
            } else {
                null
            }
            val awayScore = if (matchStatus == "finished") {
                secondTeamResult?.score
            } else {
                null
            }


            saveMatch(match.id.toString(), tournament, savedStage, savedFirstTeam, savedSecondTeam, scheduledAt, matchStatus, homeScore, awayScore)


        }
    }
}
