package com.benjk.gallexiv.controller

import com.benjk.gallexiv.config.JwtUtil
import com.benjk.gallexiv.data.entity.Image
import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.service.ImageService
import com.benjk.gallexiv.service.PostService
import com.benjk.gallexiv.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api")
class AuthenticatedController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    private val imageService: ImageService,
    private val postService: PostService,
) {

    /**
     * Auth
     */

    data class LoginRequest(val account: String, val password: String)

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

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal user: User) = user

    /**
     * Post
     */

    data class UserPostMetadataRequest(val title: String, val description: String, val tags: String)

    @PostMapping("/user/post", consumes = ["multipart/form-data"])
    fun userPost(
        @AuthenticationPrincipal user: User,
        @RequestPart("metadata") metadata: UserPostMetadataRequest,
        @RequestPart("images") images: List<MultipartFile>,
    ): UUID {
        println("Saving images")
        val savedImages = mutableSetOf<Image>()
        try {
            images.forEach { savedImages.add(imageService.saveImage(it)) }
        } catch (e: Exception) {
            println("Error saving images")
            e.printStackTrace()
        }
        return postService.createPost(metadata, savedImages.toSet(), user)
    }
}