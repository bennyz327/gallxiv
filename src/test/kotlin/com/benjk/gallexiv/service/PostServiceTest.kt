package com.benjk.gallexiv.service

import com.benjk.gallexiv.data.UserRole
import com.benjk.gallexiv.data.dto.UserPostMetadataRequest
import com.benjk.gallexiv.data.entity.Image
import com.benjk.gallexiv.data.entity.Post
import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.repository.PostRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

class PostServiceTest {
    // 建立模擬物件
    private val postRepository: PostRepository = mock()

    // 測試目標
    private lateinit var postService: PostService

    @BeforeEach
    fun setUp() {
        postService = PostService(postRepository)
    }

    @Test
    fun `創建貼文成功`() {
        // 設置測試數據
        val metadata = UserPostMetadataRequest(
            title = "測試貼文",
            description = "這是一個測試用貼文描述",
            tags = "測試,標籤"
        )

        val user = User(
            id = UUID.fromString("11111111-1111-1111-1111-111111111111"),
            account = "testUser",
            email = "test@example.com",
            username = "Test User",
            password = "encodedPassword",
            role = UserRole.USER
        )

        val image = Image(
            id = "abcdef123456",
            originalFilename = "test.jpg",
            originalExtension = "jpg"
        )

        val savedPost = Post(
            id = UUID.fromString("22222222-2222-2222-2222-222222222222"),
            title = metadata.title,
            description = metadata.description,
            tags = metadata.tags,
            user = user,
            images = setOf(image),
            isPublic = false,
            isSensitive = false
        )

        val imageSet = setOf(image)

        // 設置模擬行為
        Mockito.`when`(postRepository.save(any())).thenAnswer { _ -> savedPost }

        // 執行測試
        val result = postService.createPost(metadata, imageSet, user)

        // 驗證結果
        assert(result == savedPost.id)

        // 驗證模擬物件被正確調用
        verify(postRepository).save(any())
    }

    @Test
    fun `獲取貼文成功`() {
        // 設置測試數據
        val postId = UUID.fromString("22222222-2222-2222-2222-222222222222")
        val post = Post(
            id = postId,
            title = "測試貼文",
            description = "這是一個測試用貼文描述",
            tags = "測試,標籤",
            isPublic = true,
            isSensitive = false
        )

        // 設置模擬行為
        whenever(postRepository.findById(postId)).thenReturn(Optional.of(post))

        // 執行測試
        val result = postService.getPostById(postId)

        // 驗證結果
        assert(result.id == postId)
        assert(result.title == "測試貼文")

        // 驗證模擬物件被正確調用
        verify(postRepository).findById(postId)
    }

    @Test
    fun `獲取不存在的貼文時應拋出錯誤`() {
        // 設置測試數據
        val postId = UUID.fromString("33333333-3333-3333-3333-333333333333")

        // 設置模擬行為 - 找不到貼文
        whenever(postRepository.findById(postId)).thenReturn(Optional.empty())

        // 執行測試並驗證異常
        val exception = assertThrows<ResponseStatusException> {
            postService.getPostById(postId)
        }

        // 驗證錯誤訊息和狀態碼
        assert(exception.statusCode == HttpStatus.NOT_FOUND)
        assert(exception.reason == "Post not found")

        // 驗證模擬物件被正確調用
        verify(postRepository).findById(postId)
    }

    @Test
    fun `獲取全部貼文成功`() {
        // 設置測試數據
        val post1 = Post(
            id = UUID.fromString("22222222-2222-2222-2222-222222222222"),
            title = "測試貼文1",
            description = "這是一個測試用貼文描述1",
            tags = "測試,標籤",
            isPublic = true,
            isSensitive = false
        )

        val post2 = Post(
            id = UUID.fromString("33333333-3333-3333-3333-333333333333"),
            title = "測試貼文2",
            description = "這是一個測試用貼文描述2",
            tags = "測試,其他標籤",
            isPublic = true,
            isSensitive = false
        )

        val postList = listOf(post1, post2)

        // 設置模擬行為
        whenever(postRepository.findAll()).thenReturn(postList)

        // 執行測試
        val result = postService.getAllPosts()

        // 驗證結果
        assert(result.size == 2)
        assert(result[0].id == post1.id)
        assert(result[1].id == post2.id)

        // 驗證模擬物件被正確調用
        verify(postRepository).findAll()
    }

    @Test
    fun `獲取公開貼文成功`() {
        // 設置測試數據
        val post1 = Post(
            id = UUID.fromString("22222222-2222-2222-2222-222222222222"),
            title = "公開貼文1",
            description = "這是一個公開的貼文描述1",
            tags = "測試,公開",
            isPublic = true,
            isSensitive = false
        )

        val post2 = Post(
            id = UUID.fromString("33333333-3333-3333-3333-333333333333"),
            title = "公開貼文2",
            description = "這是一個公開的貼文描述2",
            tags = "測試,公開",
            isPublic = true,
            isSensitive = false
        )

        val publicPosts = listOf(post1, post2)

        // 設置模擬行為
        whenever(postRepository.findAllPublicPosts()).thenReturn(publicPosts)

        // 執行測試
        val result = postService.getPublicPosts()

        // 驗證結果
        assert(result.size == 2)
        assert(result[0].isPublic && !result[0].isSensitive)
        assert(result[1].isPublic && !result[1].isSensitive)

        // 驗證模擬物件被正確調用
        verify(postRepository).findAllPublicPosts()
    }
}