package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.PlayerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerRepository : JpaRepository<PlayerEntity, Long>

