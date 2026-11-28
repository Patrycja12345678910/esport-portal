package com.esportportal.service

import com.esportportal.dto.MatchResponseDto
import com.esportportal.exception.InvalidParameterException
import com.esportportal.exception.ResourceNotFoundException
import com.esportportal.persistence.entity.MatchEntity
import com.esportportal.persistence.repository.MatchRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class MatchService(
    private val matchRepository: MatchRepository
) {
    //informacje o meczach
    fun getAllMatches(date: LocalDate?,tournamentId: Long?,game:String?,status: String?,pageable: Pageable): Page<MatchResponseDto> {
       //walidacje
        if (game != null && game !in listOf("cs2", "lol")) {
            throw InvalidParameterException("Nieobsługiwana gra: $game")
        }

        if(status != null && status !in listOf("finished", "running", "not_started", "canceled")) {
            throw InvalidParameterException("Nieobsługiwany status meczu: $status")
        }

        if(tournamentId != null && tournamentId <= 0) {
            throw InvalidParameterException("Nieprawidłowe Id turnieju: $tournamentId")
        }

        val allowedSortFields = listOf("id", "scheduledAt", "status", "homeScore", "awayScore")

        for(sort in pageable.sort) {
            if(sort.property !in allowedSortFields) {
                throw InvalidParameterException(
                    "Nieprawidłowe pole sortowania: ${sort.property}"
                )
            }
        }

        val matches = when {
            game != null && status != null && tournamentId != null  && date != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentGameSlugAndStatusAndTournamentIdAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(game, status, tournamentId, date.atStartOfDay(),date.plusDays(1).atStartOfDay(), pageable)

            game != null && status != null && date != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentGameSlugAndStatusAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(game, status, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)

            game != null && tournamentId != null  && date != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentGameSlugAndTournamentIdAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(game, tournamentId, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)

            status != null && tournamentId != null  && date != null ->
                matchRepository.findByDeletedAtIsNullAndStatusAndTournamentIdAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(status, tournamentId, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)

            game != null && date != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentGameSlugAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(game, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)

            status != null && date != null ->
                matchRepository.findByDeletedAtIsNullAndStatusAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(status, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)

            tournamentId != null  && date != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentIdAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(tournamentId, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)


            game != null && status != null && tournamentId != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentGameSlugAndStatusAndTournamentId(game, status, tournamentId, pageable)

            game != null && status != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentGameSlugAndStatus(game, status, pageable)

            game != null && tournamentId != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentGameSlugAndTournamentId(game,tournamentId, pageable)

            status != null && tournamentId != null ->
                matchRepository.findByDeletedAtIsNullAndStatusAndTournamentId(status, tournamentId, pageable)

            game != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentGameSlug(game, pageable)

            status != null ->
                matchRepository.findAllByDeletedAtIsNullAndStatus(status, pageable)

            tournamentId != null ->
                matchRepository.findByDeletedAtIsNullAndTournamentId(tournamentId, pageable)

            date != null ->
                matchRepository.findByDeletedAtIsNullAndScheduledAtGreaterThanEqualAndScheduledAtLessThan(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)

            else ->
                matchRepository.findAllByDeletedAtIsNull(pageable)
        }
        return matches.map { match ->
            toDto(match)
        }

    }
//informacja o danym meczu
    fun getMatchById(id: Long): MatchResponseDto {
        val match = matchRepository.findByIdAndDeletedAtIsNull(id)

        if (match == null) {
           throw ResourceNotFoundException("Nie znaleziono meczu o ID $id")
        }
         return toDto(match)

    }
//mapuje MatchEntity na MatchResponseDto zeby uniknąć powtarzania kodu
    private fun toDto(match: MatchEntity): MatchResponseDto {
        return MatchResponseDto(
            id = requireNotNull(match.id),
            tournamentName = match.tournament?.name,
            homeTeamName = match.homeTeam?.name,
            awayTeamName = match.awayTeam?.name,
            homeScore = match.homeScore,
            awayScore = match.awayScore,
            scheduledAt = match.scheduledAt,
            status = match.status,
            gameSlug = match.tournament?.game?.slug,
            tournamentId = match.tournament?.id,
        )

    }
}
