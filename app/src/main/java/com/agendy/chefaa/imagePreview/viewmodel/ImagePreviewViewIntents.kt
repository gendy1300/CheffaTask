package com.agendy.chefaa.imagePreview.viewmodel

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner


sealed class ImagePreviewViewIntents {
    data class GetImage(
        val lifecycleOwner: LifecycleOwner,
        val context: Context,
        val currentActivity: ComponentActivity
    ) : ImagePreviewViewIntents()


    data object NavigateBack : ImagePreviewViewIntents()


    data class StartWorker(
        val context: Context,
        val imageId: Int,
        val height: Int,
        val width: Int
    ) : ImagePreviewViewIntents()

    companion object {
        const val ID_KEY = "imageId"
        const val HEIGHT_KEY = "imageHeight"
        const val WIDTH_KEY = "imageWidth"
    }

}