package com.example.testpractice.edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testpractice.data.FakePlaceRepository
import com.example.testpractice.data.local.Place
import com.example.testpractice.getOrAwaitValue
import com.example.testpractice.others.Status
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class EditViewModelTest {

    private lateinit var placeRepository: FakePlaceRepository
    private lateinit var editViewModel: EditViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val place1 = Place("Place1", "Comment1")
    private val place2 = Place("Place2", "Comment2", true)
    private val place3 = Place("Place3", "Comment3")

    @Before
    fun setupViewModel() {
        placeRepository = FakePlaceRepository()
        placeRepository.addPlaces(place1, place2, place3)
        editViewModel = EditViewModel(placeRepository)
    }

    @Test
    fun inTheCaseOfAdd_savePlace_insertCorrectPlace(){
        editViewModel.start(null) // in the case of add
        val place4 = Place("Place4", "Comment4", true)
        editViewModel.title.value = place4.title
        editViewModel.savePlace()
        val value = editViewModel.insertAndUpdateStatus.getOrAwaitValue().getContentIfNotHandled()!!
        assertThat(value.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun inTheCaseOfUpdate_savePlace_updateCorrectPlace(){
        editViewModel.start(place2.placeId) // in the case of edit
        editViewModel.title.value = "title"
        editViewModel.setHasBeenTo(true)
        editViewModel.savePlace()
        val value = editViewModel.insertAndUpdateStatus.getOrAwaitValue().getContentIfNotHandled()!!
        assertThat(value.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun inputEmptyTitle_returnsError() {
        editViewModel.start(null)
        editViewModel.title.value = ""
        editViewModel.savePlace()
        val value = editViewModel.insertAndUpdateStatus.getOrAwaitValue().getContentIfNotHandled()!!
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun inTheCaseOfInsert_deletePlace_returnsError() {
        editViewModel.start(null)
        editViewModel.deletePlace()
        val value = editViewModel.insertAndUpdateStatus.getOrAwaitValue().getContentIfNotHandled()!!
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun inTheCaseOfUpdate_deletePlace_returnsSuccess() {
        editViewModel.start(place2.placeId)
        editViewModel.deletePlace()
        val value = editViewModel.insertAndUpdateStatus.getOrAwaitValue().getContentIfNotHandled()!!
        assertThat(value.status).isEqualTo(Status.SUCCESS)
    }

}