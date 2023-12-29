package com.agendy.chefaa.imagePreview.data.remote

import com.agendy.chefaa.imageList.data.model.ImageListResponse
import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ResizeApis {

    @Multipart
    @POST("shrink")
    @Headers("Content-Type: image/jpeg", "Authorization: Basic ynwbcWf4TTTZqMy3xr9GwvkTh4nrrFST")
    suspend fun shrinkImage(@Part file: MultipartBody.Part): ImageListResponse
}