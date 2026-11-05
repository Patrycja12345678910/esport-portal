package com.esportportal.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name="games")

class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, unique = true, length = 100)
    var name: String = ""

    @Column(nullable = false, unique = true, length = 50)
    var slug: String = ""


}