package com.benjk.gallexiv.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt_secret}")
    private val secret: String? = null

    private val issuer = "gallexiv"

    fun generateToken(id: UUID, email: String? = null): String {
        return JWT.create()
            .withClaim("id", id.toString())
            .withClaim("email", email)
            .withIssuedAt(Date())
            .withIssuer(issuer)
            .sign(Algorithm.HMAC256(secret))
    }

    fun validateTokenAndGetUserId(token: String): String {
        val verifier = JWT.require(Algorithm.HMAC256(secret))
            .withIssuer(issuer)
            .build()
        val jwt = verifier.verify(token)
        return jwt.getClaim("id").asString()
    }
}