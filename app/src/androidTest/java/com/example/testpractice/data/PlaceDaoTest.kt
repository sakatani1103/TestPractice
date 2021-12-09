package com.example.testpractice.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.testpractice.data.local.Place
import com.example.testpractice.data.local.PlaceDao
import com.example.testpractice.data.local.PlaceDatabase
import com.example.testpractice.getOrAwaitValueAndroidTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PlaceDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PlaceDatabase
    private lateinit var dao: PlaceDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            PlaceDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.placeDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertPlace() = runBlockingTest {
        val place1 = Place("title1", "comment1")
        val place2 = Place("title2", "comment2", true)
        val place3 = Place("title3", "comment3")
        dao.insertPlaceItem(place1)
        dao.insertPlaceItem(place2)
        dao.insertPlaceItem(place3)

        val allPlaces = dao.observeAllPlaceItems().getOrAwaitValueAndroidTest()
        assertThat(allPlaces).isEqualTo(listOf(place1,place2,place3))
    }

    @Test
    fun insertPlaceAndDeletePlace() = runBlockingTest {
        val place1 = Place("title1", "comment1")
        dao.insertPlaceItem(place1)

        dao.deletePlaceItem(place1.placeId)

        val allPlaces = dao.observeAllPlaceItems().getOrAwaitValueAndroidTest()
        assertThat(allPlaces).doesNotContain(place1)
    }

    @Test
    fun insertPlaceAndUpdatePlace() = runBlockingTest {
        val place1 = Place("title1", "comment1")
        val place2 = Place("title2", "comment2", true)
        val place3 = Place("title3", "comment3")
        dao.insertPlaceItem(place1)
        dao.insertPlaceItem(place2)
        dao.insertPlaceItem(place3)

        val updatePlace2 = Place("title", "comment", true, place2.imageUrl, place2.placeId)
        dao.updatePlaceItem(updatePlace2)

        val allPlaces = dao.observeAllPlaceItems().getOrAwaitValueAndroidTest()
        assertThat(allPlaces).isEqualTo(listOf(place1,updatePlace2,place3))
    }

    @Test
    fun insertPlaceAndGetPlace() = runBlockingTest {
        val place1 = Place("title1", "comment1")
        val place2 = Place("title2", "comment2", true)
        val place3 = Place("title3", "comment3")
        dao.insertPlaceItem(place1)
        dao.insertPlaceItem(place2)
        dao.insertPlaceItem(place3)

        val value = dao.getPlace(place2.placeId)
        assertThat(value).isEqualTo(place2)
    }
}