package com.example.testpractice.data.local

import androidx.lifecycle.LiveData
import com.example.testpractice.data.PlaceDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceLocalDataSource internal constructor(
    private val placeDao: PlaceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlaceDataSource {
    override fun observePlaces(): LiveData<List<Place>>{
        return placeDao.observeAllPlaceItems()
    }

    override suspend fun insertPlace(place: Place) = withContext(ioDispatcher) {
        placeDao.insertPlaceItem(place)
    }

    override suspend fun updatePlace(place: Place) = withContext(ioDispatcher){
        placeDao.updatePlaceItem(place)
    }

    override suspend fun deletePlace(id: String) = withContext(ioDispatcher){
        placeDao.deletePlaceItem(id)
    }

    override suspend fun getPlace(id: String) : Place = withContext(ioDispatcher){
        return@withContext placeDao.getPlace(id)
    }

    // このあとに含まれるAPIは空で良い
}