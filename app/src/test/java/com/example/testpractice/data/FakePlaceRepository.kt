package com.example.testpractice.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testpractice.data.local.Place

class FakePlaceRepository : PlaceRepository {
    private val placeItems: MutableList<Place> = mutableListOf()
    private val observablePlaceItems = MutableLiveData<List<Place>>(placeItems)

    override fun observePlaces(): LiveData<List<Place>> {
        return observablePlaceItems
    }

    override suspend fun insertPlace(place: Place) {
        placeItems.add(place)
        observablePlaceItems.value = placeItems
    }

    override suspend fun updatePlace(place: Place) {
        var updateIndex = 0
        for ((index, elem) in placeItems.withIndex()){
            if (elem.placeId == place.placeId) updateIndex = index
        }
        placeItems[updateIndex] = place
        observablePlaceItems.value = placeItems
    }

    override suspend fun deletePlace(id: String) {
        var deleteIndex = 0
        for ((index, elem) in placeItems.withIndex()){
            if (elem.placeId == id) deleteIndex = index
        }
        placeItems.removeAt(deleteIndex)
        observablePlaceItems.value = placeItems
    }

    override suspend fun getPlace(id: String): Place {
        var getIndex = 0
        for ((index, elem) in placeItems.withIndex()){
            if (elem.placeId == id) getIndex = index
        }
        return placeItems[getIndex]
    }

    fun addPlaces(vararg places: Place){
        for (place in places) {
            placeItems.add(place)
        }
        observablePlaceItems.value = placeItems
    }
}