package com.esportportal.controller

import com.esportportal.dto.MatchResponseDto
import com.esportportal.service.MatchService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/matches")
class MatchController(
    private val matchService: MatchService
) {
    @GetMapping
    fun getAllMatches(
        @RequestParam(required = false) status: String?,
        @RequestParam(required = false) game: String?,
        @RequestParam(required = false) tournamentId: Long?,
        @RequestParam(required = false) date: LocalDate?,
        @PageableDefault(size = 20, sort = ["scheduledAt", "id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): Page<MatchResponseDto> {
        return matchService.getAllMatches(date,tournamentId,game,status, pageable)
    }

    @GetMapping("/{id}")
    fun getMatchById(@PathVariable id: Long): MatchResponseDto {
        return matchService.getMatchById(id)
    }
}