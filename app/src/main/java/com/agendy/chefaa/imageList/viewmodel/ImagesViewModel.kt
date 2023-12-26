package com.agendy.chefaa.imageList.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.Coil
import coil.imageLoader
import coil.request.ImageRequest
import com.agendy.chefaa.imageList.data.model.ImageListResponse
import com.agendy.chefaa.imageList.domain.repositories.ImagesListRepo
import com.agendy.chefaa.utils.retrofit.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(val repo: ImagesListRepo) : ViewModel() {

    private val _imagesResponse =
        mutableStateOf<ViewState<ImageListResponse>>(ViewState.NotCalled)

    val imagesResponse: State<ViewState<ImageListResponse>> = _imagesResponse


    fun processIntent(intent: ImagesViewIntent) {
        when (intent) {
            ImagesViewIntent.CallCharacters -> {
                callCharacters()
            }

            ImagesViewIntent.CallComics -> {
                callComics()
            }

            is ImagesViewIntent.DownloadImage -> downloadImageWithCoil(
                imageUrl = intent.imageUrl, imageId = intent.imageId, context = intent.context
            )
        }
    }


    private fun callCharacters() {
        viewModelScope.launch {
            _imagesResponse.value = ViewState.Loading
            _imagesResponse.value = repo.callCharacters()
        }
    }

    private fun callComics() {
        viewModelScope.launch {
            _imagesResponse.value = ViewState.Loading
            _imagesResponse.value = repo.callComics()
        }
    }


    private fun downloadImageWithCoil(
        imageUrl: String,
        imageId: String,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .build()

                val result = request.context.imageLoader.execute(request)
                val drawable = (result.drawable as? BitmapDrawable)?.bitmap

                if (drawable != null) {
                    val file = File(
                        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "$imageId.jpg"
                    )
                    val outputStream = FileOutputStream(file)
                    drawable.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    file.absolutePath
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

}
