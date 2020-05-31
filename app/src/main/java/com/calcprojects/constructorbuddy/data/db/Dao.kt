package com.calcprojects.constructorbuddy.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calcprojects.constructorbuddy.model.Model

@Dao
interface ModelDao {

    @Query("SELECT * FROM MODELS")
    fun getAll(): LiveData<List<Model>>

    @Query("SELECT * FROM MODELS WHERE _id =:id")
    fun get(id: Int): LiveData<Model>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(model: Model)

    @Query("DELETE FROM MODELS WHERE _id =:id")
    suspend fun remove(id: Int)

    @Query("DELETE FROM MODELS")
    suspend fun clearAll()

    /*@Query("UPDATE ARTICLES SET isFavourite = 0")
    suspend fun removeAllFavourites()*/

    /*@Query("SELECT * FROM ARTICLES WHERE isFavourite = 1")
    fun getFavourites(): LiveData<List<ModelApi>>*/

    /* @Query("UPDATE ARTICLES SET isFavourite=0 WHERE _id = :id")
     suspend fun removeFavourite(id: String)*/

    /* @Query("UPDATE ARTICLES SET isFavourite=1 WHERE _id = :id")
     suspend fun setFavourite(id: String)*/
}