package com.example.testpractice.list

import android.app.Application
import androidx.lifecycle.*
import com.example.testpractice.R
import com.example.testpractice.data.DefaultPlaceRepository
import com.example.testpractice.data.local.Place
import com.example.testpractice.others.Event
import com.example.testpractice.others.Result

class ListViewModel(application: Application) :  AndroidViewModel(application) {
    private val placeRepository = DefaultPlaceRepository.getRepository(application)

    private var _currentFiltering = MutableLiveData(FilterType.ALL)
    val places: LiveData<List<Place>> = _currentFiltering.switchMap { type ->
        placeRepository.observePlaces().switchMap {  filterItems(type, it) }
    }

    private val _navigateToEdit = MutableLiveData<Map<String, String?>?>()
    val navigateToEdit: LiveData<Map<String, String?>?> = _navigateToEdit

    private fun filterItems(filterType: FilterType, places: List<Place>) : LiveData<List<Place>>{
        val result = MutableLiveData<List<Place>>()
        val filteredPlaces = ArrayList<Place>()
        for(place in places){
            when(filterType){
                FilterType.GONE -> if (place.hasBeenTo) { filteredPlaces.add(place) }
                FilterType.SOMEDAY -> if(!place.hasBeenTo) { filteredPlaces.add(place) }
                else -> filteredPlaces.add(place)
            }
        }
        result.value = filteredPlaces
        return result
    }

    fun setFilter(filterType: FilterType){
        _currentFiltering.value = filterType
    }

    fun navigateToEdit(id: String) {
        _navigateToEdit.value = mutableMapOf("type" to "edit", "id" to id)
    }

    fun navigateToAdd(){
        _navigateToEdit.value = mutableMapOf("type" to "add", "id" to null)
    }

    fun doneNavigateToEdit(){
        _navigateToEdit.value = null
    }
}

enum class FilterType {
    ALL,
    SOMEDAY,
    GONE
}

