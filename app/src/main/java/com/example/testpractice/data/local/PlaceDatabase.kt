package com.example.testpractice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Place::class],
    version = 1,
    exportSchema = false
)
abstract class PlaceDatabase: RoomDatabase() {
    abstract fun PlaceDao(): PlaceDao
}