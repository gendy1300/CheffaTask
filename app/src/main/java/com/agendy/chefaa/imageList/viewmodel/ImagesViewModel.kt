package com.agendy.chefaa.imageList.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agendy.chefaa.imageList.data.model.ImageListResponse
import com.agendy.chefaa.imageList.domain.repositories.ImagesListRepo
import com.agendy.chefaa.utils.retrofit.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

}
