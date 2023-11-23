package com.hwandefan.forum.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name="user_preferences")
data class UserPreferences(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private var id: UUID? = null,
    @OneToOne
    @JoinColumn(name="user_id")
    var user: User? = null,
    @Column(name="profilePhoto")
    var profilePhoto: String? = null,
    @Column(name="userStatus")
    var userStatus: String? = null
)