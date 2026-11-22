package com.esportportal.integration.cito.dto

data class CitoMetaDto (
    val page: Int,
    val limit: Int,
    //łączna liczba drużyn
    val total: Int,
    val totalPages: Int,
){
}