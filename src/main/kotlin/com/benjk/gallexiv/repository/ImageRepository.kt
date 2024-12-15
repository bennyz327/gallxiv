package com.benjk.gallexiv.repository

import com.benjk.gallexiv.data.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, String>