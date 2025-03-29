package com.benjk.gallexiv.data.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
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

    // 關聯的貼文
    @ManyToMany
    @JoinTable(
        name = "post_image",
        joinColumns = [JoinColumn(name = "image_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "post_id", referencedColumnName = "id")],
    )
    @JsonIgnoreProperties("images")
    val refPosts: Set<Post>? = null,
)