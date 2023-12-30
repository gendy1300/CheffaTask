package com.agendy.chefaa.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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

@HiltWorker
class UploadPhotoOneTimeWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val localDataBase: ImagesDataBase,
    private val resizeRepo: ResizeRepo
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val inputData = inputData
        val imageId = inputData.getInt(ImagePreviewViewIntents.ID_KEY, 0)
        val width = inputData.getDouble(ImagePreviewViewIntents.WIDTH_KEY, 0.0)
        val height = inputData.getDouble(ImagePreviewViewIntents.HEIGHT_KEY, 0.0)


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
            resizeRepo.resizeImage(it) { progress ->

                setProgressAsync(workDataOf("progress" to progress))

                notificationBuilder.setProgress(100, progress, false)
                    .setContentText("Progress: $progress%")
                notificationManager.notify(imageId, notificationBuilder.build())

            }
        }

        notificationManager.cancel(imageId)




        return Result.success()
    }


}