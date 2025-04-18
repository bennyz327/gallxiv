package com.benjk.gallexiv.data.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.util.*

@Table(name = "post")
@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "title")
    val title: String? = null,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "tags")
    val tags: String? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User? = null,

    @ManyToMany
    @JoinTable(
        name = "post_image",
        joinColumns = [JoinColumn(name = "post_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "image_id", referencedColumnName = "id")]
    )
    @JsonIgnoreProperties("refPosts")
    val images: Set<Image>? = null,

    @Column(name = "is_public")
    val isPublic: Boolean = false,

    @Column(name = "is_sensitive")
    val isSensitive: Boolean = false,
)