package com.benjk.gallexiv.controller

import com.benjk.gallexiv.data.entity.Post
import com.benjk.gallexiv.repository.PostRepository
import com.benjk.gallexiv.service.ImageService
import com.benjk.gallexiv.service.PostService
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api")
class AnonymousController(
    private val imageService: ImageService,
    private val postService: PostService,
    private val postRepository: PostRepository,
) {

    @GetMapping("/posts")
    fun getPosts(): List<Post> {
        return postRepository.findAll()
    }

    @GetMapping("/user/{account}/post/{postId}")
    fun getPost(@PathVariable account: String, @PathVariable postId: String): Post {
        val post = postService.getPostById(UUID.fromString(postId))
        if (post.user == null) throw ResponseStatusException(HttpStatus.FORBIDDEN, "Post not found")
        if (post.user.account!!.toString() != account) throw ResponseStatusException(HttpStatus.FORBIDDEN, "Post not found")
        return post
    }

    @GetMapping("/image/{id}")
    fun getImage(@PathVariable id: String): ResponseEntity<FileSystemResource> {
        val imageFile = imageService.getImage(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found")

        val resource = FileSystemResource(imageFile)
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        return ResponseEntity.ok()
            .headers(headers)
            .body(resource)
    }
}