package com.agendy.chefaa.main

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.agendy.chefaa.imageList.data.model.ImageModel
import com.agendy.chefaa.imageList.data.offlineStorge.ImagesDataBase
import com.agendy.chefaa.imagePreview.viewmodel.ImagePreviewViewIntents
import com.agendy.chefaa.utils.getImageName
import com.agendy.chefaa.utils.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(
    val appNavigator: AppNavigator,
    private val localDataBase: ImagesDataBase
) : ViewModel() {


    val navigationChannel = appNavigator.navigationChannel


    fun getWorkInfo(context: Context): Flow<List<WorkInfo>> {
        return WorkManager.getInstance(context)
            .getWorkInfosByTagFlow(ImagePreviewViewIntents.WORKER_TAG_KEY)
    }


    fun saveImageToApp(imageUri: Uri, context: Context, onSave: (id:Int) -> Unit) {
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


              val id =  localDataBase.imagesDao.insertImages(
                    ImageModel(
                        imagePath = file.absolutePath,
                        imageCaption = "No Caption"
                    )
                )

                onSave(id.toInt())

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
