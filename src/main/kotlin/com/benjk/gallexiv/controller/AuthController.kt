package com.benjk.gallexiv.controller

import com.benjk.gallexiv.config.JwtUtil
import com.benjk.gallexiv.data.dto.ChangePasswordRequest
import com.benjk.gallexiv.data.dto.LoginRequest
import com.benjk.gallexiv.data.dto.RegisterRequest
import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

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
     * 註冊新使用者
     * 註冊成功後自動登入並返回 JWT token
     */
    @PostMapping("/auth/register")
    fun register(@RequestBody body: RegisterRequest, response: HttpServletResponse): ResponseEntity<Map<String, UUID>> {
        // 註冊新使用者
        val userId = userService.registerUser(body)
        
        // 產生 JWT token
        val token = jwtUtil.generateToken(id = userId)
        
        // 設定 cookie
        val tokenCookie = Cookie("access_token", token).apply {
            isHttpOnly = true
            maxAge = 60*60*6 // 6 hours
            path = "/"
        }
        response.addCookie(tokenCookie)
        
        // 返回使用者 ID
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(mapOf("id" to userId))
    }

    /**
     * 登出 - 清除 access_token cookie
     */
    @PostMapping("/auth/logout")
    fun logout(response: HttpServletResponse) {
        // 設置一個空的、已過期的 cookie 來覆蓋原有的 token
        val tokenCookie = Cookie("access_token", "").apply {
            isHttpOnly = true
            maxAge = 0  // 立即過期
            path = "/"
        }
        response.addCookie(tokenCookie)
    }

    /**
     * 修改用戶密碼
     * 只有登入的用戶才能修改自己的密碼
     */
    @PostMapping("/auth/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(@AuthenticationPrincipal user: User, @RequestBody request: ChangePasswordRequest) {
        userService.changePassword(user, request)
    }

    /**
     * 從 token 取得使用者資訊
     */
    @GetMapping("/me")
    fun me(@AuthenticationPrincipal user: User) = user
}