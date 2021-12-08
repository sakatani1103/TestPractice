package com.example.testpractice.edit

import android.app.Application
import androidx.lifecycle.*

import com.example.testpractice.data.DefaultPlaceRepository
import com.example.testpractice.data.PlaceRepository
import com.example.testpractice.data.local.Place
import com.example.testpractice.others.Event
import kotlinx.coroutines.launch
import com.example.testpractice.others.Result

class EditViewModel(private val placeRepository: PlaceRepository): ViewModel() {
    private val _insertAndUpdateStatus = MutableLiveData<Event<Result<Place>>>()
    val insertAndUpdateStatus: LiveData<Event<Result<Place>>> = _insertAndUpdateStatus

    var saveType = SaveType.INSERT
    var imageUrl = MutableLiveData<String>()
    var title = MutableLiveData<String>()
    var comment = MutableLiveData<String>()
    var placeId = MutableLiveData<String>()
    var hasBeenTo = MutableLiveData(false)
    var isNewTask = MutableLiveData(true)

    fun start(id: String?){
        if (id != null) {
            placeId.value = id!!
            isNewTask.value = false
            saveType = SaveType.UPDATE

            viewModelScope.launch {
                val data = placeRepository.getPlace(id)
                imageUrl.value = data.imageUrl
                title.value = data.title
                comment.value = data.comment
                hasBeenTo.value = data.hasBeenTo
            }
        }
    }

    fun savePlace() {
        val insertTitle = title.value!!
        val insertComment = comment.value ?: ""
        val insertHasBeenTo = hasBeenTo.value ?: false
        val insertImageUrl = imageUrl.value ?: "no_image"
        if (insertTitle.isEmpty()){
           _insertAndUpdateStatus.value = Event(Result.error("タイトルが空です。", Place(insertTitle, insertComment)))
            return
        }
        if (isNewTask.value!!){
            val insert = Place(insertTitle, insertComment, insertHasBeenTo, insertImageUrl)
            insertPlace(insert)
            _insertAndUpdateStatus.value = Event(Result.success(insert))
        } else {
            val update = Place(insertTitle, insertComment, insertHasBeenTo, insertImageUrl, placeId.value!!)
            updatePlace(update)
            _insertAndUpdateStatus.value = Event(Result.success(update))
        }
    }

    private fun insertPlace(place: Place) {
        viewModelScope.launch {
            placeRepository.insertPlace(place)
        }
    }

    private fun updatePlace(place: Place) {
        viewModelScope.launch {
            placeRepository.updatePlace(place)
        }
    }

    fun deletePlace() {
        if (saveType == SaveType.INSERT) {
            _insertAndUpdateStatus.value = Event(Result.error("この場所は登録されていません。", null))
            return
        }
        viewModelScope.launch {
            placeRepository.deletePlace(placeId.value!!)
        }
        _insertAndUpdateStatus.value = Event(Result.success(null))
    }

    fun setHasBeenTo(boolean: Boolean) {
        hasBeenTo.value = boolean
    }
}

@Suppress("UNCHECKED_CAST")
class EditViewModelFactory(
    private val placeRepository: PlaceRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        (EditViewModel(placeRepository) as T)
}

enum class SaveType { INSERT, UPDATE}