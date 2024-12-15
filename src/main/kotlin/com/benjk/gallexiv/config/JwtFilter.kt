package com.benjk.gallexiv.config

import com.auth0.jwt.exceptions.JWTVerificationException
import com.benjk.gallexiv.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*


@Component
class JwtFilter(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = getTokeFromRequest(request)
        token?.let {
            try {
                val tokenId = jwtUtil.validateTokenAndGetUserId(it)
                val user = userService.findById(UUID.fromString(tokenId))
                val detail = User.builder()
                    .username(user.id.toString())
                    .password(user.password)
                    .roles(user.role.name)
                    .build()
                if (SecurityContextHolder.getContext().authentication == null) {
                    SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, detail.password, detail.authorities)
                }
            } catch (exc: JWTVerificationException) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token")
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun getTokeFromRequest(request: HttpServletRequest): String? {
        val tokenKey = "access_token"
        val cookie = request.cookies?.find { it.name == tokenKey }
        return cookie?.value
    }
}

