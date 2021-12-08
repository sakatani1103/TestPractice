package com.example.testpractice.list

import androidx.lifecycle.*
import com.example.testpractice.data.PlaceRepository
import com.example.testpractice.data.local.Place
import com.example.testpractice.others.Event

class ListViewModel(private val placeRepository: PlaceRepository) : ViewModel() {

    var currentFiltering = MutableLiveData(FilterType.ALL)
    val places: LiveData<List<Place>> = currentFiltering.switchMap { type ->
        placeRepository.observePlaces().switchMap { filterItems(type, it) }
    }

    private val _navigateToEdit = MutableLiveData<Event<Map<String, String?>>>()
    val navigateToEdit: LiveData<Event<Map<String, String?>>> = _navigateToEdit

    private fun filterItems(filterType: FilterType, places: List<Place>): LiveData<List<Place>> {
        val result = MutableLiveData<List<Place>>()
        val filteredPlaces = ArrayList<Place>()
        for (place in places) {
            when (filterType) {
                FilterType.GONE -> if (place.hasBeenTo) {
                    filteredPlaces.add(place)
                }
                FilterType.SOMEDAY -> if (!place.hasBeenTo) {
                    filteredPlaces.add(place)
                }
                else -> filteredPlaces.add(place)
            }
        }
        result.value = filteredPlaces
        return result
    }

    fun setFilter(filterType: FilterType) {
        currentFiltering.value = filterType
    }

    fun navigateToEdit(id: String) {
        _navigateToEdit.value = Event(mutableMapOf("type" to "edit", "id" to id))
    }

    fun navigateToAdd() {
        _navigateToEdit.value = Event(mutableMapOf("type" to "add", "id" to null))
    }

}

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory(
    private val placeRepository: PlaceRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (ListViewModel(placeRepository) as T)
}

enum class FilterType {
    ALL,
    SOMEDAY,
    GONE
}

