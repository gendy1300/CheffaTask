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

}