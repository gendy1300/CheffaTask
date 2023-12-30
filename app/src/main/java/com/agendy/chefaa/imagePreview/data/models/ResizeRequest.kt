package com.agendy.chefaa.imagePreview.data.models


data class ResizeRequest(
    val resize: ResizeObject,
)

data class ResizeObject(
    val method: String = "fit",
    val width: Int,
    val height: Int
)