package com.example.testpractice.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testpractice.data.FakePlaceRepository
import com.example.testpractice.data.local.Place
import com.example.testpractice.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListViewModelTest {

    private lateinit var placeRepository: FakePlaceRepository
    private lateinit var listViewModel: ListViewModel

    private val place1 = Place("Place1", "Comment1")
    private val place2 = Place("Place2", "Comment2", true)
    private val place3 = Place("Place3", "Comment3")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        placeRepository = FakePlaceRepository()
        placeRepository.addPlaces(place1, place2, place3)
        listViewModel = ListViewModel(placeRepository)
    }

    @Test
    fun navigateToAdd_setNewAddEvent(){
        listViewModel.navigateToAdd()
        val value = listViewModel.navigateToEdit.getOrAwaitValue().getContentIfNotHandled()
        assertThat(value).isEqualTo(mutableMapOf("type" to "add", "id" to null))
    }

    @Test
    fun navigateToEdit_setEditEvent() {
        listViewModel.navigateToEdit(place2.placeId)
        val value = listViewModel.navigateToEdit.getOrAwaitValue().getContentIfNotHandled()
        assertThat(value).isEqualTo(mutableMapOf("type" to "edit", "id" to place2.placeId))
    }


    @Test
    fun setFilterAll_setAllItems() {
        listViewModel.setFilter(FilterType.ALL)
        val value = listViewModel.places.getOrAwaitValue()
        assertThat(value).isEqualTo(listOf(place1, place2, place3))
    }

    @Test
    fun setFilterGone_setHasBeenToItems() {
        listViewModel.setFilter(FilterType.GONE)
        val value = listViewModel.places.getOrAwaitValue()
        assertThat(value).isEqualTo(listOf(place2))
    }

    @Test
    fun setFilterSomeDay_setHasNotBeenToItems() {
        listViewModel.setFilter(FilterType.SOMEDAY)
        val value = listViewModel.places.getOrAwaitValue()
        assertThat(value).isEqualTo(listOf(place1, place3))
    }
}