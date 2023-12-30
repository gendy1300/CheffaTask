package com.agendy.chefaa.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.agendy.chefaa.R
import com.agendy.chefaa.imageList.data.offlineStorge.ImagesDataBase
import com.agendy.chefaa.imagePreview.domain.repository.ResizeRepo
import com.agendy.chefaa.imagePreview.viewmodel.ImagePreviewViewIntents
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

@HiltWorker
class UploadPhotoOneTimeWork @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val localDataBase: ImagesDataBase,
    private val resizeRepo: ResizeRepo
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val inputData = inputData
        val imageId = inputData.getInt(ImagePreviewViewIntents.ID_KEY, 0)
        val width = inputData.getInt(ImagePreviewViewIntents.WIDTH_KEY, 0)
        val height = inputData.getInt(ImagePreviewViewIntents.HEIGHT_KEY, 0)


        val imageModel = localDataBase.imagesDao.getImageWithId(imageId)?.first()


        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "channel_id",
            "Upload",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "channel_id")
            .setContentTitle("Uploading ${imageModel?.imageCaption}")
            .setContentText("Progress...")
            .setProgress(100, 0, false)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)

        notificationManager.notify(imageId, notificationBuilder.build())


        imageModel?.imagePath?.let {
            val response = resizeRepo.uploadImageToTinyPng(it) { progress ->

                setProgressAsync(workDataOf("progress" to progress))

                notificationBuilder.setProgress(100, progress, false)
                    .setContentText("Progress: $progress%")
                notificationManager.notify(imageId, notificationBuilder.build())

            }


            val image = response.output?.url?.let { url ->
                resizeRepo.resizeImage(
                    imageUrl = url, width = width,
                    height = height
                )
            }

            if (image?.isSuccessful == true) {
                val imageBytes = image.body()?.bytes()

                if (imageBytes != null) {

                    val file = File(
                        appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "$imageId.jpg"
                    )


                    try {
                        val outputStream: OutputStream = FileOutputStream(file)
                        outputStream.write(imageBytes)
                        outputStream.close()
                        val imagePath = file.absolutePath


                            localDataBase.imagesDao.updateImage(
                                imageModel.copy(imagePath = imagePath)
                            )

                    } catch (e: IOException) {
                        e.printStackTrace()

                    }
                }
            }
        }





        notificationManager.cancel(imageId)




        return Result.success()
    }


}