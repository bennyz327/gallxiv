package com.benjk.gallexiv.data.entity

import jakarta.persistence.*

@Table(name = "image")
@Entity
class Image(
    @Id
    @Column(name = "id")
    val id: String? = null, // 儲存在 /images/{hash}的前兩碼/{hash}

    @Column(name = "original_filename")
    val originalFilename: String? = null,

    @Column(name = "original_extension")
    val originalExtension: String? = null,
)