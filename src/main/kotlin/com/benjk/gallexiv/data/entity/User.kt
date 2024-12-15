package com.benjk.gallexiv.data.entity

import com.benjk.gallexiv.data.UserRole
import jakarta.persistence.*
import java.util.*

@Table(name = "`user`")
@Entity(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "account", unique = true, updatable = false)
    val account: String? = null,

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "username")
    var username: String? = null,

    @Column(name = "password")
    var password: String? = null,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,
)