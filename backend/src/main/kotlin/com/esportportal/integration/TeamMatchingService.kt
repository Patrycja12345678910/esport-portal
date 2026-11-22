package com.esportportal.integration

import com.esportportal.persistence.entity.TeamEntity
import com.esportportal.persistence.entity.TeamExternalIdEntity
import com.esportportal.persistence.repository.TeamRepository
import com.esportportal.persistence.repository.TeamExternalIdRepository
import org.springframework.stereotype.Service
import com.esportportal.integration.cito.dto.CitoTeamDto
import org.springframework.transaction.annotation.Transactional


@Service
class TeamMatchingService (
    private val teamRepository: TeamRepository,
    private val externalIdRepository: TeamExternalIdRepository,
    private val nameNormalizer: TeamNameNormalizer
){

    fun znajdzMapowanieCito(externalId: String) : TeamExternalIdEntity? {
        return externalIdRepository.findBySourceAndExternalId(
            "cito",
            externalId
        )
    }

        fun znajdzDruzynePoNazwie(nazwa: String): TeamEntity? {
           val szukanaNazwa = nameNormalizer.normalizujNazwe(nazwa)

            if (szukanaNazwa.isBlank()) {
                return null
            }

           val druzyny = teamRepository.findByGameSlug("cs2")


            val pasujace = druzyny.filter { team ->
                nameNormalizer.normalizujNazwe(team.name) == szukanaNazwa

            }
            return pasujace.singleOrNull()
        }

    fun znajdzKandydatowPoNazwie(nazwa: String): List<TeamEntity> {
        val szukanaNazwa = nameNormalizer.normalizujRozszerzonaNazwe(nazwa)

        if (szukanaNazwa.isBlank()) {
            return emptyList()
        }

        val druzyny = teamRepository.findByGameSlug("cs2")

        return druzyny.filter { team ->
            nameNormalizer.normalizujRozszerzonaNazwe(team.name) == szukanaNazwa
        }
    }

    @Transactional
    fun dopasujDruzyneCito(citoTeam: CitoTeamDto): TeamEntity? {
        val mapowanie = znajdzMapowanieCito(citoTeam.id)

        if(mapowanie != null) {
            return mapowanie.team
        }

        val druzyna = znajdzDruzynePoNazwie(citoTeam.name)

        if(druzyna != null) {
            val noweMapowanie = TeamExternalIdEntity()

            noweMapowanie.team = druzyna
            noweMapowanie.source = "cito"
            noweMapowanie.externalId = citoTeam.id

            externalIdRepository.save(noweMapowanie)
        }
        return druzyna
    }

    @Transactional
    fun zatwierdzMapowanieCito(externalId: String, teamId: Long): TeamExternalIdEntity {
        val mapowanie = znajdzMapowanieCito(externalId)

        if(mapowanie != null) {
            if(mapowanie.team?.id == teamId) {
                return mapowanie
            }

            throw IllegalStateException("Identyfikator Cito jest już przypisany do innej drużyny")
        }

        val druzyny = teamRepository.findByGameSlug("cs2")

        val druzyna = druzyny.find { team ->
            team.id == teamId
        } ?: throw IllegalArgumentException("Nie znaleziono drużyny")

        val noweMapowanie = TeamExternalIdEntity()

        noweMapowanie.team = druzyna
        noweMapowanie.source = "cito"
        noweMapowanie.externalId = externalId

        return externalIdRepository.save(noweMapowanie)


    }


    }


