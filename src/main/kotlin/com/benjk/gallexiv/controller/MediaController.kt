package com.benjk.gallexiv.controller

import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.service.ImageService
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Tag(name = "Media")
@RestController
@RequestMapping("/api")
class MediaController(
    private val imageService: ImageService,
) {

    private val logger = LoggerFactory.getLogger(MediaController::class.java)

    /**
     * 取得圖片ID取得檔案
     * 只處理圖片檔案
     */
    @GetMapping("/image/{postId}/{id}")
    fun getImage(
        @AuthenticationPrincipal user: User?,
        @PathVariable postId: String,
        @PathVariable id: String,
    ): ResponseEntity<FileSystemResource>? {
        imageService.getImage(id)?.let { imageInfo ->
            val belongsToPosts = imageInfo.refPosts
            if (belongsToPosts.isNullOrEmpty()) {
                println("Not Allowed to access image (No post found)")
                return null;
            }
            val belongPost = belongsToPosts.filter { post -> post.id == UUID.fromString(postId) }
            if (belongPost.isEmpty() || belongPost.size != 1) {
                println("Not Allowed to access image (Post not found)")
                return null
            }
            if (!belongPost.first().isPublic) {
                println("Not Allowed to access image (Post not public)")
                return null
            }
            println("Allowed to access image")

            imageService.getImageFile(imageInfo.id!!, imageInfo.originalExtension)?.let { resource ->
                val headers = HttpHeaders()
                when (imageInfo.originalExtension) {
                    "jpg" -> headers.contentType = MediaType.IMAGE_JPEG
                    "jpeg" -> headers.contentType = MediaType.IMAGE_JPEG
                    "png" -> headers.contentType = MediaType.IMAGE_PNG
                }
                return ResponseEntity.ok().headers(headers).body(resource)
            } ?: {
                logger.error("Image record at DB, But not found in file system")
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found")
            }
        } ?: {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found")
        }
        return ResponseEntity.notFound().build()
    }
}