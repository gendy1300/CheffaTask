package com.agendy.chefaa.imageList.domain.repositories

import com.agendy.chefaa.imageList.data.model.ImageListResponse
import com.agendy.chefaa.utils.retrofit.ViewState

interface ImagesListRepo {

    suspend fun callCharacters(): ViewState<ImageListResponse>

    suspend fun callComics(): ViewState<ImageListResponse>


}
