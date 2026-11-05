package com.esportportal.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name="players")

class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, length = 100)
    var nickname:String=""

    @Column(name = "first_name", length = 100)
    var firstName:String? = null

    @Column(name = "last_name", length = 100)
    var lastName:String? = null

    @Column(length = 100)
    var nationality:String?= null


}