package com.agendy.chefaa.imageList.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageModel(
    @PrimaryKey val id: Int,
    val imagePath: String,
    val imageCaption: String
)
