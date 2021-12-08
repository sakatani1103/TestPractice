package com.example.testpractice.data

import androidx.lifecycle.LiveData
import com.example.testpractice.data.local.Place

class DefaultPlaceRepository (private val placeLocalDataSource: PlaceDataSource) : PlaceRepository {

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