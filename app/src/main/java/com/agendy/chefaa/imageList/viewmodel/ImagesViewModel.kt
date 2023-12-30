package com.agendy.chefaa.imageList.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import com.agendy.chefaa.imageList.data.model.ImageListResponse
import com.agendy.chefaa.imageList.data.model.ImageModel
import com.agendy.chefaa.imageList.data.model.ImagesResult
import com.agendy.chefaa.imageList.data.offlineStorge.ImagesDataBase
import com.agendy.chefaa.imageList.domain.repositories.ImagesListRepo
import com.agendy.chefaa.utils.navigation.AppNavigator
import com.agendy.chefaa.utils.navigation.screens.HomeScreens
import com.agendy.chefaa.utils.retrofit.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val repo: ImagesListRepo,
    private val localDataBase: ImagesDataBase,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val _imagesResponse =
        mutableStateOf<ViewState<ImageListResponse>>(ViewState.NotCalled)

    val imagesResponse: State<ViewState<ImageListResponse>> = _imagesResponse

    val images: Flow<List<ImageModel>?> = localDataBase.imagesDao.getImages()


    fun processIntent(intent: ImagesViewIntent) {
        when (intent) {
            ImagesViewIntent.CallCharacters -> {
                callCharacters()
            }

            ImagesViewIntent.CallComics -> {
                callComics()
            }

            is ImagesViewIntent.DownloadImage -> downloadImages(
                items = intent.images,
                context = intent.context
            )

            is ImagesViewIntent.NavigateToImagePreview -> navigateToImagePreview(intent.id)
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


    private fun downloadImages(items: List<ImagesResult>, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            items.forEach { result ->

                if (localDataBase.imagesDao.getImageWithId(result.id).isNullOrEmpty()) {
                    val image = result.thumbnail
                    downloadImageWithCoil(
                        imageUrl = "${image?.path}.${image?.extension}",
                        imageId = result.id,
                        context = context
                    )
                }

            }
        }
    }

    private fun downloadImageWithCoil(
        imageUrl: String,
        imageId: Int,
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
                    localDataBase.imagesDao.insertImages(
                        ImageModel(
                            imageId,
                            file.absolutePath,
                            "No Caption",
                            width = drawable.width,
                            height = drawable.height

                        )
                    )

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun navigateToImagePreview(imageId: Int) = viewModelScope.launch {
        appNavigator.navigateTo(HomeScreens.ResizeScreen(imageId))
    }

}
