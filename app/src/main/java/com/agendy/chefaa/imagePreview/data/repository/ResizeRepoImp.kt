package com.agendy.chefaa.imagePreview.data.repository

import android.webkit.MimeTypeMap
import com.agendy.chefaa.imagePreview.data.models.ResizeObject
import com.agendy.chefaa.imagePreview.data.models.ResizeRequest
import com.agendy.chefaa.imagePreview.data.models.ShrinkResponse
import com.agendy.chefaa.imagePreview.data.remote.ResizeApis
import com.agendy.chefaa.imagePreview.domain.repository.ResizeRepo
import com.agendy.chefaa.utils.retrofit.ProgressRequestBody
import com.agendy.chefaa.utils.retrofit.SafeApiCall
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.util.Base64
import javax.inject.Inject

class ResizeRepoImp @Inject constructor(
    private var api: ResizeApis
) : ResizeRepo, SafeApiCall {


    override suspend fun uploadImageToTinyPng(
        imageFilePath: String,
        onProgress: (Int) -> Unit
    ): ShrinkResponse {
        val imageFile = File(imageFilePath)

        val requestFile = ProgressRequestBody(
            imageFile,
            getContentType(imageFilePath) ?: "image/*",
            progressListener = { bytesWritten, contentLength ->

                val progress = (bytesWritten.toFloat() / contentLength * 100).toInt()
                onProgress(progress)
            }
        )


//        val filePart = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

        return api.shrinkImage(
            auth = getHeaderDigest(),
            imageBytes = requestFile,
            contentType = getContentType(imageFilePath) ?: "image/*"
        )
    }

    override suspend fun resizeImage(
        imageUrl: String,
        width: Int,
        height: Int
    ): Response<ResponseBody> {

        val resizeRequest = ResizeRequest(resize = ResizeObject(width = width, height = height))
        return api.resizeImage(
            url = imageUrl,
            auth = getHeaderDigest(),
            resizeBody = resizeRequest,
        )
    }


    private fun getHeaderDigest(apiKey: String = "ynwbcWf4TTTZqMy3xr9GwvkTh4nrrFST"): String {
        val authString = "api:$apiKey"
        val authStringBytes = authString.toByteArray()
        val encodedAuth = Base64.getEncoder().encodeToString(authStringBytes)
        return "Basic $encodedAuth"
    }


    private fun getContentType(fileName: String): String? {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileName)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
    }

}