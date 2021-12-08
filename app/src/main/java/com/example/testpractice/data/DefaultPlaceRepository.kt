package com.example.testpractice.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.testpractice.data.local.Place
import com.example.testpractice.data.local.PlaceDatabase
import com.example.testpractice.data.local.PlaceLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultPlaceRepository (private val placeLocalDataSource: PlaceDataSource) : PlaceRepository {


    companion object {
        @Volatile
        private var INSTANCE: DefaultPlaceRepository? = null

        fun getRepository(application: Application): DefaultPlaceRepository {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(application, PlaceDatabase::class.java,"Place.db")
                    .build()
                DefaultPlaceRepository(PlaceLocalDataSource(database.PlaceDao())).also {
                    INSTANCE = it
                }
            }
        }
    }

    override fun observePlaces(): LiveData<List<Place>>{
        return placeLocalDataSource.observePlaces()
    }

    override suspend fun insertPlace(place: Place) {
        placeLocalDataSource.insertPlace(place)
    }

    override suspend fun updatePlace(place: Place) {
        placeLocalDataSource.updatePlace(place)
    }

    override suspend fun deletePlace(id: String) {
        placeLocalDataSource.deletePlace(id)
    }

    override suspend fun getPlace(id: String) : Place {
        return placeLocalDataSource.getPlace(id)
    }
}