package com.esportportal.persistence.repository

import com.esportportal.persistence.entity.SyncImportLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

interface SyncImportLogRepository: JpaRepository<SyncImportLogEntity, Long> {

}