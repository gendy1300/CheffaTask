package com.agendy.chefaa.imageList.viewmodel

sealed class ImagesViewIntent {
    data object CallCharacters : ImagesViewIntent()
    data object CallComics : ImagesViewIntent()

}
