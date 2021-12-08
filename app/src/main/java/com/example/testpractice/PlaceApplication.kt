package com.example.testpractice

import android.app.Application
import com.example.testpractice.data.PlaceRepository
import timber.log.Timber

class PlaceApplication : Application() {

    val placeRepository: PlaceRepository
        get() = ServiceLocator.providePlaceRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}