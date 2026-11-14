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
import com.esportportal.persistence.repository.SyncStateRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import org.springframework.scheduling.annotation.Scheduled
import org.slf4j.LoggerFactory
import com.esportportal.persistence.entity.SyncStateEntity
import com.esportportal.integration.pandascore.dto.PandaScoreMatchDto
import com.esportportal.integration.pandascore.dto.PandaScoreIncidentDto
import kotlin.math.log
import org.springframework.context.annotation.Profile

@Service
class PandaScoreSyncService(
    private val teamRepository: TeamRepository,
    private val tournamentRepository: TournamentRepository,
    private val gameRepository: GameRepository,
    private val pandaScoreClient: PandaScoreClient,
    private val tournamentStageRepository: TournamentStageRepository,
    private val matchRepository: MatchRepository,
    private val syncStateRepository: SyncStateRepository

) {

    private val logger = LoggerFactory.getLogger(PandaScoreSyncService::class.java)

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

    fun saveMatch(externalId: String, tournament: TournamentEntity, stage: TournamentStageEntity?, homeTeam: TeamEntity?, awayTeam: TeamEntity?, scheduledAt: LocalDateTime?, status: String, homeScore: Int?, awayScore:Int?): MatchEntity {
        val match = matchRepository.findByExternalId(externalId)
            ?: MatchEntity()

        match.externalId = externalId
        match.deletedAt = null
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
    fun markMatchAsDeleted(externalId: String) {
        val match = matchRepository.findByExternalId(externalId)
            ?: return

        match.deletedAt = Instant.now()
        matchRepository.save(match)
    }

    fun getGame(slug: String): GameEntity {
        return gameRepository.findBySlug(slug)
            ?: error("Nie znaleziono gry: $slug")

    }
    fun processMatch(match: PandaScoreMatchDto, gameSlug: String): Boolean {
        val game = getGame(gameSlug)
        val expectedApiSlug = if(gameSlug == "cs2") {
            "cs-go"
        } else {
            "league-of-legends"
        }

        if(match.videogame?.slug != expectedApiSlug) {
            logger.warn("Mecz ${match.id} należy do innej gry")
            return false
        }

        logger.info(
            "Mecz ID=${match.id}, oczekiwana gra=$gameSlug, gra z API=${match.videogame?.slug}"
        )

        val serie = match.serie
        val serieId = serie?.id

        if (serieId == null) {
            logger.warn("Brakuje serii w meczu ID=${match.id}")
            return false
        }

        val stage = match.tournament
        val stageId = stage?.id

        if(stageId == null) {
            logger.warn("Brakuje etapu w meczu ID=${match.id}")
            return false
        }

        val stageName = stage.name

        if(stageName == null) {
            logger.warn("Brakuje nazwy etapu w meczu ID=${match.id}")
            return false
        }

        val opponents = match.opponents

        if(opponents == null) {
            logger.warn("Brakuje listy drużyn w meczu ID=${match.id}")
            return false
        }

        val firstTeam = opponents.getOrNull(0)?.opponent


        val secondTeam = opponents.getOrNull(1)?.opponent


        val leagueName = match.league?.name
            ?.takeIf { it.isNotBlank() } ?: "Nieznana liga"

        val tournamentName = if (serie.name.isNullOrBlank()) {
            leagueName
        } else {
            "$leagueName ${serie.name}"
        }

        val tournament = saveTournament(
            serieId.toString(),
            tournamentName,
            game
        )

        val savedStage = saveStage(
            stageId.toString(),
            stageName,
            tournament
        )

        val savedFirstTeam = firstTeam?.let { team ->
            team.name?.let { name ->
                saveTeam(team.id.toString(), name)
            }
        }


        val savedSecondTeam = secondTeam?.let { team ->
            team.name?.let { name ->
                saveTeam(team.id.toString(), name)
            }
        }

        val scheduledAt = match.scheduledAt?.let {
            LocalDateTime.ofInstant(Instant.parse(it), ZoneOffset.UTC)
        }

        val matchStatus = match.status ?: "unknown"

        val homeScore = if (firstTeam != null &&
            (matchStatus == "finished" || matchStatus == "running")
        ) {
            match.results?.find { it.teamId == firstTeam.id }?.score
        } else {
            null
        }

        val awayScore = if (secondTeam != null &&
            (matchStatus == "finished" || matchStatus == "running")
        ) {
            match.results?.find { it.teamId == secondTeam.id }?.score
        } else {
            null
        }

        saveMatch(
            match.id.toString(),
            tournament,
            savedStage,
            savedFirstTeam,
            savedSecondTeam,
            scheduledAt,
            matchStatus,
            homeScore,
            awayScore
        )

        return true
    }
    fun syncSingleMatch(id: Long, gameSlug: String): Boolean {
        val match = pandaScoreClient.pobierzMeczPoId(id)
            ?: return false

        return processMatch(match,gameSlug)
    }
    @Synchronized
    fun syncMatches(gameSlug: String = "cs2") {
        val czasRozpoczecia = Instant.now()

        val source = "pandascore_$gameSlug"
        val syncState = syncStateRepository.findBySource(source)
            ?: SyncStateEntity()
        syncState.source = source

        val ostatniaSynchronizacja = syncState.lastSuccessfulSync
        logger.info("Ostatnia synchronizacja: $ostatniaSynchronizacja")

        val matches: List<PandaScoreMatchDto> =
            if (ostatniaSynchronizacja != null) {

                val incidents = mutableListOf<PandaScoreIncidentDto>()
                var strona = 1

                while (true) {
                    val pobraneIncydenty = pandaScoreClient.pobierzZmianyMeczow(
                        ostatniaSynchronizacja,
                        gameSlug,
                        strona
                    )

                    incidents.addAll(pobraneIncydenty)
                    pobraneIncydenty.forEach { incident ->
                        logger.info(
                            "Pobrano zmianę meczu: ID=${incident.id}, typ=${incident.changeType}, modifiedAt=${incident.modifiedAt}"
                        )
                    }
                    logger.info("Changes, strona $strona: ${pobraneIncydenty.size} incydentów")

                    if (pobraneIncydenty.size < 100) {
                        break
                    }

                    strona++
                }
                var stronaNowych = 1

                while (true) {
                    val noweMecze = pandaScoreClient.pobierzNoweMecze(
                        ostatniaSynchronizacja,
                        gameSlug,
                        stronaNowych
                    )

                    incidents.addAll(noweMecze)
                    logger.info("Additions, strona $stronaNowych: ${noweMecze.size} incydentów")

                    if (noweMecze.size < 100) {
                        break
                    }

                    stronaNowych++
                }

                incidents.mapNotNull { incident ->
                    if (incident.changeType == "update" || incident.changeType == "creation") {
                        incident.`object`
                    } else {
                        null
                    }
                }
            } else {
                val upcomingMatches = pandaScoreClient.pobierzNadchodzaceMecze(gameSlug)
                val pastMatches = pandaScoreClient.pobierzZakonczoneMecze(gameSlug)
                val runningMatches = pandaScoreClient.pobierzTrwajaceMecze(gameSlug)

                upcomingMatches + pastMatches + runningMatches
            }

        logger.info("Synchronizacja: pobrano ${matches.size} meczów")

        logger.info(
            "Czy pobrano mecz 1795395: ${matches.any { it.id == 1795395L }}"
        )

        var pominietoMecze = false
        var licznik = 0

        for (match in matches) {
            val zapisano = processMatch(match, gameSlug)

            if (!zapisano) {
                pominietoMecze = true
                licznik++
                logger.warn("Nie udało się przetworzyć meczu ID=${match.id}, gra=$gameSlug")
            }
        }
        logger.info("Licznik: $licznik")

        if (ostatniaSynchronizacja != null) {
            var stronaUsuniec = 1

            while (true) {
                val usunieteMecze = pandaScoreClient.pobierzUsunieteMecze(
                    ostatniaSynchronizacja,
                    gameSlug,
                    stronaUsuniec
                )
                logger.info("Liczba usuniętych meczów na stronie: ${usunieteMecze.size}")

                for (incident in usunieteMecze) {
                    if (incident.type == "match" && incident.changeType == "deletion") {
                        markMatchAsDeleted(incident.id.toString())
                    }
                }

                if (usunieteMecze.size < 100) {
                    break
                }

                stronaUsuniec++
            }
        }
        if (pominietoMecze) {
            throw IllegalStateException(
                "Pominięto mecze z niepełnymi danymi. Synchronizacja nie została w pełni zakończona."
            )
        }

        syncState.lastSuccessfulSync = czasRozpoczecia
        syncStateRepository.save(syncState)
    }

    @Profile("!test")
    //15 minut przerwy od zakończenia jednej synchronizacji do rozpoczęcia następnej
    @Scheduled(fixedDelay = 900000)
    fun automatycznaSynchronizacja() {
        try {
            logger.info("Rozpoczynam automatyczną synchronizację meczów CS2")
            syncMatches("cs2")
            logger.info("Synchronizacja zakończona pomyślnie")
        } catch (e: Exception) {
            logger.error("Synchronizacja nie powiodła się", e)
        }
    }

}
