package com.example.testpractice.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaceItem(placeItem: Place)

    @Query("DELETE FROM place WHERE placeId = :id")
    suspend fun deletePlaceItem(id: String)

    @Update
    suspend fun updatePlaceItem(placeItem: Place)

    @Query("SELECT * FROM place")
    fun observeAllPlaceItems(): LiveData<List<Place>>

    @Query("SELECT * FROM place WHERE placeId = :id")
    suspend fun getPlace(id: String): Place
}