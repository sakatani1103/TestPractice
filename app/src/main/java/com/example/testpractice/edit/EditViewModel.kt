package com.example.testpractice.edit

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testpractice.data.DefaultPlaceRepository
import com.example.testpractice.data.local.Place
import com.example.testpractice.others.Event
import kotlinx.coroutines.launch
import com.example.testpractice.others.Result

class EditViewModel(application: Application): AndroidViewModel(application) {
    private val placeRepository = DefaultPlaceRepository.getRepository(application)

    private val _insertAndUpdateStatus = MutableLiveData<Event<Result<Place>>>()
    val insertAndUpdateStatus: LiveData<Event<Result<Place>>> = _insertAndUpdateStatus

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
        viewModelScope.launch {
            placeRepository.deletePlace(placeId.value!!)
        }
        _insertAndUpdateStatus.value = Event(Result.success(null))
    }

    fun setHasBeenTo(boolean: Boolean) {
        hasBeenTo.value = boolean
    }


}