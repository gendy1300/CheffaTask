package com.agendy.chefaa.di

import com.agendy.chefaa.imageList.data.remote.ImagesListApis
import com.agendy.chefaa.imageList.data.repository.ImagesListRepoImpl
import com.agendy.chefaa.imageList.domain.repositories.ImagesListRepo
import com.agendy.chefaa.utils.retrofit.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {


    @Singleton
    @Provides
    fun provideMainApis(remoteDataSource: RetrofitClient): ImagesListApis {
        return remoteDataSource.buildApi(ImagesListApis::class.java,"https://gateway.marvel.com")
    }


    @Singleton
    @Provides
    fun provideMainRepo(api: ImagesListApis): ImagesListRepo {
        return ImagesListRepoImpl(api)
    }


}