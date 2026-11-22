package com.esportportal.integration.cito

import com.esportportal.integration.TeamMatchingService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Value

@Service
class CitoSyncService (
    private val citoClient: CitoClient,
    private val teamMatchingService: TeamMatchingService,
    @Value("\${cito.sync.page-size}") private val limit: Int,
    @Value("\${cito.sync.max-pages}") private val maxStron: Int

) {
    fun synchronizujDruzyny() {


        var strona = 1
        var liczbaDruzyn = 0
        var liczbaDopasowanychDruzyn = 0

//paginacja - kolejne strony z cito
        while (strona <= maxStron) {
            val response = citoClient.pobierzDruzyny(strona, limit)
            val druzyny = response.data

            if (druzyny.isEmpty()) {
                break
            }

            liczbaDruzyn += druzyny.size

            druzyny.forEach { druzyna ->
                val wynik = teamMatchingService.dopasujDruzyneCito(druzyna)

                if (wynik != null) {
                    liczbaDopasowanychDruzyn ++
                }
            }

            if(strona >= response.meta.totalPages) {
                break
            }
            strona++
        }

        println("Pobrano drużyn: $liczbaDruzyn")
        println("Dopasowano drużyn: $liczbaDopasowanychDruzyn")
    }


    fun sprawdzDopasowania() {
        val druzyny = citoClient.pobierzDruzyny().data

        druzyny.forEach { druzyna ->
            val mapowanie = teamMatchingService.znajdzMapowanieCito(druzyna.id)

            val znaleziona = if (mapowanie != null) {
                mapowanie.team
            } else {
                teamMatchingService.znajdzDruzynePoNazwie(druzyna.name)
            }

            if(znaleziona != null) {
                println("Cito: ${druzyna.name} -> ${znaleziona.name}")
            } else {
                val kandydaci = teamMatchingService.znajdzKandydatowPoNazwie(druzyna.name)

                if(kandydaci.isEmpty()) {
                    println("Cito: ${druzyna.name} -> BRAK DOPASOWANIA")
                } else {
                    println("Cito: ${druzyna.name} -> KANDYDACI: ${kandydaci.map { it.name }}")
                }
            }

        }
    }

}
