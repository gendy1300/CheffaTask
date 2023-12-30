package com.agendy.chefaa.imagePreview.data.remote

import com.agendy.chefaa.imagePreview.data.models.ResizeRequest
import com.agendy.chefaa.imagePreview.data.models.ShrinkResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ResizeApis {

    @POST("shrink")
    @Streaming
    suspend fun shrinkImage(
        @Header("Authorization") auth: String,
        @Body imageBytes: RequestBody,
        @Header("Content-Type") contentType: String,
    ): ShrinkResponse


    @POST
    suspend fun resizeImage(
        @Url url: String,
        @Header("Authorization") auth: String,
        @Body resizeBody: ResizeRequest
    ): Response<ResponseBody>
}