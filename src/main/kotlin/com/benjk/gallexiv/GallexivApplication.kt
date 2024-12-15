package com.benjk.gallexiv

import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.repository.UserRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class GallexivApplication

fun main(args: Array<String>) {
    val context = runApplication<GallexivApplication>(*args)

    val userRepository = context.getBean(UserRepository::class.java)
    val passwordEncoder = context.getBean(BCryptPasswordEncoder::class.java)
    if (userRepository.findAll().isEmpty()) {
        userRepository.save(
            User(
                account = "admin",
                password = passwordEncoder.encode("admin"),
                username = "admin",
            )
        )
    }
}
