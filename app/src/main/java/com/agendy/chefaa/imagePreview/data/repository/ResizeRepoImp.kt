package com.agendy.chefaa.imagePreview.data.repository

import com.agendy.chefaa.imagePreview.data.remote.ResizeApis
import com.agendy.chefaa.imagePreview.domain.repository.ResizeRepo
import com.agendy.chefaa.utils.retrofit.SafeApiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ResizeRepoImp @Inject constructor(
    private var api: ResizeApis
) : ResizeRepo, SafeApiCall {
    override suspend fun resizeImage(imageFilePath: String) = safeApiCall {
        val imageFile = File(imageFilePath)
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", imageFile.name, requestBody)

        api.shrinkImage(filePart)
    }


}