package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.GameEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<GameEntity, Long>{
    fun findBySlug(slug: String): GameEntity?
}



