package com.benjk.gallexiv.service

import com.benjk.gallexiv.controller.AuthenticatedController
import com.benjk.gallexiv.data.entity.Image
import com.benjk.gallexiv.data.entity.Post
import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.repository.PostRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class PostService(private val postRepository: PostRepository) {

    @Transactional
    fun createPost(
        metadata: AuthenticatedController.UserPostMetadataRequest,
        savedImageSet: Set<Image>,
        existUser: User,
    ): UUID {
        val post = Post(
            title = metadata.title,
            description = metadata.description,
            tags = metadata.tags,
            images = savedImageSet,
            user = existUser,
        )
        postRepository.save(post)
        return post.id!!
    }

    fun getPostById(postId: UUID): Post {
        return postRepository.findById(postId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found") }
    }
}