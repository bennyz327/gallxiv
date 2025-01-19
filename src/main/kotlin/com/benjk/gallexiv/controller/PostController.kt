package com.benjk.gallexiv.controller

import com.benjk.gallexiv.data.dto.UserPostMetadataRequest
import com.benjk.gallexiv.data.entity.Image
import com.benjk.gallexiv.data.entity.Post
import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.service.ImageService
import com.benjk.gallexiv.service.PostService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Tag(name = "Post")
@RestController
@RequestMapping("/api")
class PostController(
    private val imageService: ImageService,
    private val postService: PostService,
) {

    /**
     * 取得所有貼文
     * 會判斷是否為登入狀態，若是則回傳所有貼文，否則回傳公開貼文
     */
    @GetMapping("/posts")
    fun getPosts(@AuthenticationPrincipal user: User?): List<Post> {
        user?.let {
            return postService.getAllPosts()
        } ?: run {
            return postService.getPublicPosts()
        }
    }

    /**
     * 透過ID取得貼文
     */
    @GetMapping("/user/{account}/post/{postId}")
    fun getPost(@PathVariable account: String, @PathVariable postId: String): Post {
        val post = postService.getPostById(UUID.fromString(postId))
        if (post.user == null) throw ResponseStatusException(HttpStatus.FORBIDDEN, "Post not found")
        if (post.user.account!!.toString() != account) throw ResponseStatusException(HttpStatus.FORBIDDEN, "Post not found")
        return post
    }

    /**
     * 上傳貼文
     * 只有登入的使用者可以上傳貼文
     */
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
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving images")
        }
        return postService.createPost(metadata, savedImages.toSet(), user)
    }
}