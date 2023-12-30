package com.agendy.chefaa.imageList.data.offlineStorge.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.agendy.chefaa.imageList.data.model.ImageModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertImages(image: ImageModel): Long

    @Query("SELECT * FROM imagemodel")
    fun getImages(): Flow<List<ImageModel>?>

    @Query("SELECT * FROM imagemodel WHERE id = :id")
    fun getImageWithId(id: Int): List<ImageModel>?

    @Query("UPDATE ImageModel SET imageCaption = :caption WHERE id = :id")
    suspend fun updateImageCaption(id: Int, caption: String)

    @Update
    suspend fun updateImage(image: ImageModel): Int
}