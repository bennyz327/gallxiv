package com.benjk.gallexiv.repository

import com.benjk.gallexiv.data.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, UUID> {

    @Query("""
        SELECT p
        FROM Post p
        WHERE p.isPublic = true
        AND p.isSensitive = false
    """)
    fun findAllPublicPosts(): List<Post>
}