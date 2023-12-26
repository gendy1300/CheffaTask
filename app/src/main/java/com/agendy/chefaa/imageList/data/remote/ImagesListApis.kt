package com.agendy.chefaa.imageList.data.remote

import com.agendy.chefaa.imageList.data.model.ImageListResponse
import retrofit2.http.GET

interface ImagesListApis {

    @GET("v1/public/characters")
    suspend fun callCharacters(): ImageListResponse

    @GET("v1/public/comics")
    suspend fun callComics(): ImageListResponse


}