package com.example.testpractice.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.testpractice.data.local.Place

interface PlaceRepository {
    fun observePlaces(): LiveData<List<Place>>
    suspend fun insertPlace(place: Place)

    suspend fun updatePlace(place: Place)

    suspend fun deletePlace(id: String)

    suspend fun getPlace(id: String) : Place

}