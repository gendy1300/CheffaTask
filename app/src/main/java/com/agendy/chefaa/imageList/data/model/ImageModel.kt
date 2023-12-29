package com.agendy.chefaa.imageList.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String = "",
    val imageCaption: String = ""
)
