package com.agendy.chefaa.imagePreview.domain.repository

import com.agendy.chefaa.imageList.data.model.ImageListResponse
import com.agendy.chefaa.utils.retrofit.ViewState

interface ResizeRepo {

    suspend fun resizeImage(imageFilePath: String,onProgress:(Int) ->Unit): ViewState<ImageListResponse>
}