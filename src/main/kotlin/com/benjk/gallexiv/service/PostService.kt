package com.benjk.gallexiv.service

import com.benjk.gallexiv.data.dto.UserPostMetadataRequest
import com.benjk.gallexiv.data.entity.Image
import com.benjk.gallexiv.data.entity.Post
import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.repository.PostRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

/**
 * 貼文資產服務層
 */
@Service
class PostService(private val postRepository: PostRepository) {

    /**
     * 創建貼文
     */
    @Transactional
    fun createPost(
        metadata: UserPostMetadataRequest,
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

    /**
     * 透過貼文 ID 取得貼文
     */
    fun getPostById(postId: UUID): Post {
        return postRepository.findById(postId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found") }
    }

    /**
     * 取得所有貼文
     */
    fun getAllPosts(): List<Post> {
        return postRepository.findAll()
    }

    /**
     * 取得公開貼文
     * 公開貼文不僅僅是 isPublic 為 true，還要檢查是否為敏感貼文，敏感貼文不會被公開
     */
    fun getPublicPosts(): List<Post> {
        return postRepository.findAllPublicPosts()
    }
}