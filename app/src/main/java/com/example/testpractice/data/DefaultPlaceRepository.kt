package com.example.testpractice.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.testpractice.data.local.Place
import com.example.testpractice.data.local.PlaceDatabase
import com.example.testpractice.data.local.PlaceLocalDataSource

class DefaultPlaceRepository private constructor(application: Application){
    private val placeLocalDataSource: PlaceLocalDataSource

    companion object {
        @Volatile
        private var INSTANCE: DefaultPlaceRepository? = null

        fun getRepository(application: Application): DefaultPlaceRepository {
            return INSTANCE ?: synchronized(this) {
                DefaultPlaceRepository(application).also {
                    INSTANCE = it
                }
            }
        }
    }

    init {
        val database = Room.databaseBuilder(application.applicationContext,
        PlaceDatabase::class.java, "Place.tb")
            .build()
        placeLocalDataSource = PlaceLocalDataSource(database.PlaceDao())
    }

    fun observePlaces(): LiveData<List<Place>>{
        return placeLocalDataSource.observePlaces()
    }

    suspend fun insertPlace(place: Place) {
        placeLocalDataSource.insertPlace(place)
    }

    suspend fun updatePlace(place: Place) {
        placeLocalDataSource.updatePlace(place)
    }

    suspend fun deletePlace(id: String) {
        placeLocalDataSource.deletePlace(id)
    }

    suspend fun getPlace(id: String) : Place {
        return placeLocalDataSource.getPlace(id)
    }
}