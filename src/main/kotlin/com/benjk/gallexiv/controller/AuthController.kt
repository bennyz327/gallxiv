package com.benjk.gallexiv.controller

import com.benjk.gallexiv.config.JwtUtil
import com.benjk.gallexiv.data.dto.LoginRequest
import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "Auth")
@RestController
@RequestMapping("/api")
class AuthController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
) {

    /**
     * 登入獲取 token
     */
    @PostMapping("/auth/login")
    fun login(@RequestBody body: LoginRequest, response: HttpServletResponse) {
        val user = userService.findByAccountPassword(body.account, body.password)
        val token = jwtUtil.generateToken(id = user.id!!)
        val tokenCookie = Cookie("access_token", token).apply {
            isHttpOnly = true
            maxAge = 60*60*6 // 6 hours
            path = "/"
        }
        response.addCookie(tokenCookie)
    }

    /**
     * 從 token 取得使用者資訊
     */
    @GetMapping("/me")
    fun me(@AuthenticationPrincipal user: User) = user
}