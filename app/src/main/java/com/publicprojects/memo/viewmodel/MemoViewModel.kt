package com.publicprojects.memo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.publicprojects.memo.R
import com.publicprojects.memo.util.Utils
import com.publicprojects.memo.util.isAfter
import kotlinx.coroutines.launch

class MemoViewModel(application: Application) : AndroidViewModel(application) {

    private val _eventName: MutableLiveData<String> = MutableLiveData("")

    private val _eventDate: MutableLiveData<Long> = MutableLiveData(0)
    val eventDate: LiveData<Long> = _eventDate

    private val _eventStartTime: MutableLiveData<String> = MutableLiveData("")
    val eventStartTime: LiveData<String> = _eventStartTime

    private val _eventEndTime: MutableLiveData<String> = MutableLiveData("")
    val eventEndTime: LiveData<String> = _eventEndTime

    private val _eventIsValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val eventIsValid: LiveData<Boolean> = _eventIsValid

    val endTimeError: MutableLiveData<String> = MutableLiveData("")
    val startTimeError: MutableLiveData<String> = MutableLiveData("")
    private val eventDesc: MutableLiveData<String> = MutableLiveData("")

    fun saveMemo() {
        //
    }

    fun setPickedDate(date: Long) {
        viewModelScope.launch {
            _eventDate.value = date
            validateFields()
        }
    }

    fun setPickedStartTime(time: String) {
        viewModelScope.launch {
            _eventStartTime.value = time
            validateFields()
        }
    }

    fun setPickedEndTime(time: String) {
        viewModelScope.launch {
            _eventEndTime.value = time
            validateFields()
        }
    }

    fun setEventName(name: String) {
        viewModelScope.launch {
            _eventName.value = name
            validateFields()
        }
    }

    fun setEventDesc(desc: String) {
        viewModelScope.launch {
            eventDesc.value = desc
        }
    }

    private fun validateFields() {
        val startT = _eventStartTime.value!!
        val endT = _eventEndTime.value!!
        val date = _eventDate.value!!
        _eventIsValid.value = _eventName.value!!.isNotBlank() && date > 0 &&
                startT.isNotBlank() && endT.isNotBlank()
        if (startT isAfter endT) {
            _eventIsValid.value = false
            endTimeError.value = getApplication<Application?>().getString(R.string.err_end_time_error)
        } else {
            endTimeError.value = ""
        }
        if (Utils.isPast(date, startT)) {
            _eventIsValid.value = false
            startTimeError.value = getApplication<Application?>().getString(R.string.err_start_time_error)
        } else {
            startTimeError.value = ""
        }
    }
}