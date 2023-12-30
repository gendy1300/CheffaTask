package com.agendy.chefaa.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.agendy.chefaa.imageList.data.offlineStorge.ImagesDataBase
import com.agendy.chefaa.imageList.data.remote.ImagesListApis
import com.agendy.chefaa.imageList.data.repository.ImagesListRepoImpl
import com.agendy.chefaa.imageList.domain.repositories.ImagesListRepo
import com.agendy.chefaa.imagePreview.data.remote.ResizeApis
import com.agendy.chefaa.imagePreview.data.repository.ResizeRepoImp
import com.agendy.chefaa.imagePreview.domain.repository.ResizeRepo
import com.agendy.chefaa.imagePreview.viewmodel.ImagePreviewViewModel
import com.agendy.chefaa.utils.navigation.AppNavigator
import com.agendy.chefaa.utils.retrofit.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {


    @Singleton
    @Provides
    fun provideMainApis(remoteDataSource: RetrofitClient): ImagesListApis {
        return remoteDataSource.buildApi(ImagesListApis::class.java, "https://gateway.marvel.com")
    }


    @Singleton
    @Provides
    fun provideMainRepo(api: ImagesListApis): ImagesListRepo {
        return ImagesListRepoImpl(api)
    }

    @Singleton
    @Provides
    fun provideResizeApis(remoteDataSource: RetrofitClient): ResizeApis {
        return remoteDataSource.buildApi(ResizeApis::class.java, "https://api.tinify.com/")
    }


    @Singleton
    @Provides
    fun provideResizeRepo(api: ResizeApis): ResizeRepo {
        return ResizeRepoImp(api)
    }

    @Provides
    @Singleton
    fun provideOfflineOrdersDatabase(app: Application): ImagesDataBase {
        return Room.databaseBuilder(
            app,
            ImagesDataBase::class.java,
            ImagesDataBase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }


}