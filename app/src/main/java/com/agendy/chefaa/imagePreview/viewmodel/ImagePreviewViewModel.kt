package com.agendy.chefaa.imagePreview.viewmodel

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.agendy.chefaa.imageList.data.model.ImageModel
import com.agendy.chefaa.imageList.data.offlineStorge.ImagesDataBase
import com.agendy.chefaa.imagePreview.domain.repository.ResizeRepo
import com.agendy.chefaa.utils.UploadPhotoOneTimeWork
import com.agendy.chefaa.utils.navigation.AppNavigator
import com.agendy.chefaa.utils.navigation.screens.HomeScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class ImagePreviewViewModel @Inject constructor(
    private val repo: ResizeRepo,
    private val localDataBase: ImagesDataBase,
    private val savedStateHandle: SavedStateHandle,
    private val appNavigator: AppNavigator
) : ViewModel() {


    val imageModel = mutableStateOf(ImageModel())
    var dominantColor = mutableStateOf(Color.Black)
    fun processIntent(intent: ImagePreviewViewIntents) {
        when (intent) {
            ImagePreviewViewIntents.NavigateBack -> navigateBack()


            is ImagePreviewViewIntents.GetImage -> getCurrentImageModel(
                currentActivity = intent.currentActivity,
                lifecycleOwner = intent.lifecycleOwner,
                context = intent.context
            )



            is ImagePreviewViewIntents.StartWorker -> startWorker(
                imageId = intent.imageId,
                context = intent.context,
                width = intent.width,
                height = intent.height
            )
        }
    }

    private fun saveImage(imageUri: Uri, context: Context, onImageSaved: (id: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {


            val inputStream = context.contentResolver.openInputStream(imageUri)
            val outputStream: OutputStream


            try {
                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "${getImageName(context.contentResolver, imageUri)}.jpg"
                )
                outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()

                val id = localDataBase.imagesDao.insertImages(
                    ImageModel(
                        imagePath = file.absolutePath,
                        imageCaption = "No Caption"
                    )
                )

                onImageSaved(id.toInt())

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun getImageName(contentResolver: ContentResolver, imageUri: Uri): String? {
        var imageName: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val cursor: Cursor? = contentResolver.query(imageUri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex: Int =
                    it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                imageName = it.getString(columnIndex)
            }
        }
        return imageName
    }


    private fun extractDominantColorFromFilePath(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(filePath)
            dominantColor.value = extractDominantColor(bitmap)
        } else {
            print("file not found")
        }
    }


    private fun extractDominantColor(imageBitmap: Bitmap): Color {
        val palette = Palette.from(imageBitmap).generate()
        val dominantSwatch = palette.dominantSwatch
        val color = dominantSwatch?.rgb ?: android.graphics.Color.BLACK
        return Color(color)
    }


    private fun getCurrentImageModel(
        lifecycleOwner: LifecycleOwner,
        currentActivity: ComponentActivity?,
        context: Context
    ) {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && currentActivity is Activity) {
                val intent = currentActivity.intent
                if (intent != null && intent.action == Intent.ACTION_SEND) {

                    val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)


                    if (imageUri != null) {
                        saveImage(imageUri, context) { id ->
                            localDataBase.imagesDao.getImageWithId(id)?.first()
                                ?.let { image ->
                                    imageModel.value = image
                                    extractDominantColorFromFilePath(image.imagePath)
                                }
                        }
                    }

                }
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    savedStateHandle.get<String>(HomeScreens.IMAGE_ID_KEY)?.toInt()
                        ?.let { imageId ->
                            localDataBase.imagesDao.getImageWithId(
                                imageId
                            )?.first()?.let { image ->
                                imageModel.value = image
                                extractDominantColorFromFilePath(image.imagePath)
                            }
                        }
                }

            }
        })

    }

    private fun startWorker(imageId: Int, context: Context, width: Double, height: Double) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = workDataOf(
            ImagePreviewViewIntents.ID_KEY to imageId,
            ImagePreviewViewIntents.WIDTH_KEY to width,
            ImagePreviewViewIntents.HEIGHT_KEY to height
        )

        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadPhotoOneTimeWork>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }



    private fun navigateBack() = viewModelScope.launch { appNavigator.navigateBack() }
}