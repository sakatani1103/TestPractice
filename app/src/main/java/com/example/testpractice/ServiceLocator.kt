package com.example.testpractice

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.testpractice.data.DefaultPlaceRepository
import com.example.testpractice.data.PlaceDataSource
import com.example.testpractice.data.PlaceRepository
import com.example.testpractice.data.local.PlaceDatabase
import com.example.testpractice.data.local.PlaceLocalDataSource

object ServiceLocator {
    private var database: PlaceDatabase? = null
    private var lock = Any()

    @Volatile
    var placeRepository: PlaceRepository? = null
        @VisibleForTesting set

    fun providePlaceRepository(context: Context): PlaceRepository {
        synchronized(this) {
            return placeRepository ?: createPlaceRepository(context)
        }
    }

    private fun createPlaceRepository(context: Context): PlaceRepository {
        val newRepository = DefaultPlaceRepository(createPlaceLocalDataSource(context))
        placeRepository = newRepository
        return newRepository
    }

    private fun createPlaceLocalDataSource(context: Context): PlaceDataSource {
        val database = database ?: createDatabase(context)
        return PlaceLocalDataSource(database.placeDao())
    }

    private fun createDatabase(context: Context): PlaceDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            PlaceDatabase::class.java, "Place.db"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            placeRepository = null
        }
    }
}