package com.agendy.chefaa.imageList.data.repository

import com.agendy.chefaa.imageList.data.remote.ImagesListApis
import com.agendy.chefaa.imageList.domain.repositories.ImagesListRepo
import com.agendy.chefaa.utils.retrofit.SafeApiCall
import javax.inject.Inject

class ImagesListRepoImpl @Inject constructor(
    private var api: ImagesListApis
) : ImagesListRepo, SafeApiCall {


    override suspend fun callCharacters() = safeApiCall {
        api.callCharacters()
    }

    override suspend fun callComics() = safeApiCall {
        api.callComics()

    }
}
