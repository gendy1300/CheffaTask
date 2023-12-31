package com.agendy.chefaa.imageList.viewmodel

import android.content.Context
import com.agendy.chefaa.imageList.data.model.ImagesResult

sealed class ImagesViewIntent {
    data object CallCharacters : ImagesViewIntent()
    data object CallComics : ImagesViewIntent()

    data class DownloadImage(val images: List<ImagesResult>, val context: Context) :
        ImagesViewIntent()


    data class NavigateToImagePreview(val id: Int) : ImagesViewIntent()

    data class SaveImage(val id: Int,val context: Context) : ImagesViewIntent()

}
