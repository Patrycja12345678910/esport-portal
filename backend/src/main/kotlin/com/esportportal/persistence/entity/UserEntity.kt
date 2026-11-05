package com.esportportal.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name="users")

class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, unique = true, length = 255)
    var email: String = ""

    @Column(name = "password_hash", nullable = false, length=255)
    var passwordHash: String = ""

    @Column(length=50, nullable = false)
    var role: String = ""

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
}