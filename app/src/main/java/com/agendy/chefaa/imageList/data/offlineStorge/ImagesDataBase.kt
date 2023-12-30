package com.agendy.chefaa.imageList.data.offlineStorge

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agendy.chefaa.imageList.data.model.ImageModel
import com.agendy.chefaa.imageList.data.offlineStorge.daos.ImagesDao


@Database(entities = [ImageModel::class], version = 2)
abstract class ImagesDataBase : RoomDatabase() {
    abstract val imagesDao: ImagesDao

    companion object {
        const val DATABASE_NAME = "ImagesDB"
    }

}