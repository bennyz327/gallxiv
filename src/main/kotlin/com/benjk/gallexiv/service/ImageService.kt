package com.benjk.gallexiv.service

import com.benjk.gallexiv.data.entity.Image
import com.benjk.gallexiv.repository.ImageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest

@Service
class ImageService(
    private val imageRepository: ImageRepository,
    @Value("\${image.base-path:images}") private val basePath: String,
) {

    @Transactional
    fun saveImage(file: MultipartFile): Image {
        val hash = file.bytes.sha256Hex()
        val subDir = hash.substring(0, 2)
        val saveDir = File("$basePath/$subDir").apply { mkdirs() }
        val saveFile = File(saveDir, hash)

        // 若檔案已存在則不重複儲存(可依需求調整邏輯)
        if (!saveFile.exists()) {
            FileOutputStream(saveFile).use { fos ->
                fos.write(file.bytes)
            }
        }

        val image = imageRepository.findById(hash).orElseGet {
            imageRepository.save(Image(id = hash, originalFilename = file.originalFilename))
        }
        return image
    }


    fun getImage(id: String): File? {
        val imgOpt = imageRepository.findById(id)
        if (imgOpt.isPresent) {
            val subDir = id.substring(0, 2)
            val filePath = "$basePath/$subDir/$id"
            val imageFile = File(filePath)
            return if (imageFile.exists()) imageFile else null
        }
        return null
    }


    fun ByteArray.sha256Hex(): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(this)
        return digest.joinToString("") { "%02x".format(it) }
    }
}