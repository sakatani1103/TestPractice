package com.example.testpractice.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.testpractice.data.local.Place
import com.example.testpractice.data.local.PlaceDatabase
import com.example.testpractice.data.local.PlaceLocalDataSource
import com.example.testpractice.getOrAwaitValueAndroidTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class PlaceLocalDataSourceTest {
    private lateinit var localDataSource: PlaceLocalDataSource
    private lateinit var database: PlaceDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlaceDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        localDataSource = PlaceLocalDataSource(
            database.placeDao(),
            Dispatchers.Main
        )

    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun insertPlace_retrievesPlaces() = runBlocking {
        val place1 = Place("title1", "comment1")
        val place2 = Place("title2", "comment2", true)
        val place3 = Place("title3", "comment3")
        localDataSource.insertPlace(place1)
        localDataSource.insertPlace(place2)
        localDataSource.insertPlace(place3)

        val places = localDataSource.observePlaces().getOrAwaitValueAndroidTest()
        assertThat(places).isEqualTo(listOf(place1, place2, place3))
    }

    @Test
    fun insertPlaceAndUpdatePlace_retrievesCorrectPlaces() = runBlocking {
        val place1 = Place("title1", "comment1")
        val place2 = Place("title2", "comment2", true)
        val place3 = Place("title3", "comment3")
        localDataSource.insertPlace(place1)
        localDataSource.insertPlace(place2)
        localDataSource.insertPlace(place3)

        val updatePlace2 = Place("title", "comment", true, place2.imageUrl, place2.placeId)
        localDataSource.updatePlace(updatePlace2)

        val places = localDataSource.observePlaces().getOrAwaitValueAndroidTest()
        assertThat(places).isEqualTo(listOf(place1, updatePlace2, place3))
    }

    @Test
    fun insertPlaceAndDeletePlace_retrievesCorrectPlaces() = runBlocking {
        val place1 = Place("title1", "comment1")
        val place2 = Place("title2", "comment2", true)
        val place3 = Place("title3", "comment3")
        localDataSource.insertPlace(place1)
        localDataSource.insertPlace(place2)
        localDataSource.insertPlace(place3)

        localDataSource.deletePlace(place2.placeId)

        val places = localDataSource.observePlaces().getOrAwaitValueAndroidTest()
        assertThat(places).doesNotContain(place2)
    }

    @Test
    fun insertPlace_retrievesCertainPlace() = runBlocking {
        val place1 = Place("title1", "comment1")
        val place2 = Place("title2", "comment2", true)
        val place3 = Place("title3", "comment3")
        localDataSource.insertPlace(place1)
        localDataSource.insertPlace(place2)
        localDataSource.insertPlace(place3)

        val value = localDataSource.getPlace(place2.placeId)
        assertThat(value).isEqualTo(place2)
    }
}