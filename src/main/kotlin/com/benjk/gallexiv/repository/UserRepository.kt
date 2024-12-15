package com.benjk.gallexiv.repository

import com.benjk.gallexiv.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByEmail(email: String): Optional<User>
    fun findByAccount(account: String): Optional<User>
}
