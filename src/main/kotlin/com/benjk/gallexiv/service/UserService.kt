package com.benjk.gallexiv.service

import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    fun findById(id: UUID): User = userRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") }

    fun findByAccountPassword(account: String, password: String): User = userRepository
        .findByAccount(account)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") }
        .also {
            if (!passwordEncoder.matches(password, it.password)) {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password")
            }
        }
}