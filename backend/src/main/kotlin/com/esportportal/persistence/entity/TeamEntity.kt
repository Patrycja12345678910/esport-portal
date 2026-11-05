package com.esportportal.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name="teams")

class TeamEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 255)
    var name: String = "",

    @Column(length = 50)
    var acronym: String? = null,

    @Column(length = 100)
    var country: String? = null,

    @Column(name="logo_url", length = 500)
    var logoUrl: String? = null,

    @Column(name = "external_id", unique = true ,length = 100)
    var externalId: String? = null

)

