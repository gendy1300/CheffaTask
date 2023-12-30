package com.agendy.chefaa.imagePreview.data.models

data class ShrinkResponse(
    val input: Input? = null,
    val output: Output? = null
)

data class Output(
    val height: Int? = null,
    val ratio: Double? = null,
    val size: Int? = null,
    val type: String? = null,
    val url: String? = null,
    val width: Int? = null
)


data class Input(
    val size: Int? = null,
    val type: String? = null
)