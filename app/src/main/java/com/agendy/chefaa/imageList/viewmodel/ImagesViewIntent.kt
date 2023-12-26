package com.agendy.chefaa.imageList.viewmodel

import android.content.Context

sealed class ImagesViewIntent {
    data object CallCharacters : ImagesViewIntent()
    data object CallComics : ImagesViewIntent()

    data class DownloadImage(val imageUrl: String, val imageId: String, val context: Context) :
        ImagesViewIntent()

}
