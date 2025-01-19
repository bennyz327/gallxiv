package com.benjk.gallexiv.service

import com.benjk.gallexiv.data.entity.Image
import com.benjk.gallexiv.repository.ImageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import kotlin.jvm.optionals.getOrNull

@Service
class ImageService(
    private val imageRepository: ImageRepository,
    @Value("\${image.base-path:images}") private val basePath: String,
) {

    @Transactional
    fun saveImage(file: MultipartFile): Image {
        val hash = file.bytes.sha256Hex()
        val ext = file.originalFilename?.substringAfterLast(".")
        val subDir = getSubDirFromHash(hash)
        val saveDir = File("$basePath/$subDir").apply { mkdirs() }
        // 儲存檔案 (如果有，則包含原本副檔名)
        val saveFile = File("$saveDir/$hash.$ext")
        // 若檔案已存在則不重複儲存(可依需求調整邏輯)
        if (!saveFile.exists()) {
            FileOutputStream(saveFile).use { fos ->
                fos.write(file.bytes)
            }
        }
        val image = imageRepository.findById(hash).orElseGet {
            imageRepository.save(
                Image(
                    id = hash,
                    originalFilename = file.originalFilename,
                    originalExtension = ext,
                )
            )
        }
        return image
    }


    /**
     * 透過ID取得圖片資料，若不存在則回傳null
     */
    fun getImage(imageHash: String) = imageRepository.findById(imageHash).getOrNull()


    /**
     * 透過檔案系統取得圖片檔案
     */
    fun getImageFile(
        imageId: String,
        imageExtension: String?,
    ): FileSystemResource? {
        val filePath = imageExtension?.let {
            "$basePath/${getSubDirFromHash(imageId)}/$imageId.$it"
        } ?: "$basePath/${getSubDirFromHash(imageId)}/$imageId"
        val imageFile = File(filePath)
        return if (imageFile.exists()) FileSystemResource(imageFile) else null
    }


    /**
     * SHA256 hash 計算
     */
    private fun ByteArray.sha256Hex(): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(this)
        return digest.joinToString("") { "%02x".format(it) }
    }


    /**
     * HASH 或 HASH.ext 轉子目錄的邏輯
     */
    private fun getSubDirFromHash(hash: String): String {
        return hash.substring(0, 2)
    }
}