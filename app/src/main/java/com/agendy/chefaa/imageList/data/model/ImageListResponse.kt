package com.agendy.chefaa.imageList.data.model

data class ImageListResponse(
    val data: Data? = Data(),
    val status: String? = ""
)


data class Data(
    val count: Int? = 0,
    val limit: Int? = 0,
    val offset: Int? = 0,
    val results: List<ImagesResult>? = listOf(),
    val total: Int? = 0
)


data class ImagesResult(
    val id: Int = 0,
    val thumbnail: Thumbnail? = Thumbnail(),
)


data class Thumbnail(
    val extension: String? = null,
    val path: String? = null
)
