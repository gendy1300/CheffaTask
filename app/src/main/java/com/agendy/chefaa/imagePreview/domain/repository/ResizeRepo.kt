package com.agendy.chefaa.imagePreview.domain.repository

import com.agendy.chefaa.imagePreview.data.models.ShrinkResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface ResizeRepo {

    suspend fun uploadImageToTinyPng(
        imageFilePath: String,
        onProgress: (Int) -> Unit
    ): ShrinkResponse

    suspend fun resizeImage(
        imageUrl: String,
        width: Int,
        height: Int
    ): Response<ResponseBody>


}