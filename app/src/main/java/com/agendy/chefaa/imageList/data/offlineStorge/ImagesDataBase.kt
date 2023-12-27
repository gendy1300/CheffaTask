package com.agendy.chefaa.imageList.data.offlineStorge

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agendy.chefaa.imageList.data.model.ImageModel
import com.agendy.chefaa.imageList.data.offlineStorge.daos.ImagesProductsDao


@Database(entities = [ImageModel::class], version = 1)
abstract class ImagesDataBase : RoomDatabase() {
    abstract val imagesProductsDao: ImagesProductsDao

    companion object {
        const val DATABASE_NAME = "ImagesDB"
    }

}