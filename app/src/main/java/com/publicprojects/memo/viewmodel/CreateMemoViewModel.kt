package com.publicprojects.memo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.publicprojects.memo.R
import com.publicprojects.memo.model.Memo
import com.publicprojects.memo.model.repository.MemoRepository
import com.publicprojects.memo.util.Utils
import com.publicprojects.memo.util.isAfter
import com.publicprojects.memo.view.sealed.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMemoViewModel @Inject constructor(
    application: Application,
    private val repository: MemoRepository
) : AndroidViewModel(application) {

    private val _eventName: MutableLiveData<String> = MutableLiveData("")

    private val _eventDate: MutableLiveData<String> = MutableLiveData("")
    val eventDate: LiveData<String> = _eventDate

    private val _eventStartTime: MutableLiveData<String> = MutableLiveData("")
    val eventStartTime: LiveData<String> = _eventStartTime

    private val _eventEndTime: MutableLiveData<String> = MutableLiveData("")
    val eventEndTime: LiveData<String> = _eventEndTime

    private val _eventIsValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val eventIsValid: LiveData<Boolean> = _eventIsValid

    val endTimeError: MutableLiveData<String> = MutableLiveData("")
    val startTimeError: MutableLiveData<String> = MutableLiveData("")
    private val eventDesc: MutableLiveData<String> = MutableLiveData("")

    private val _createMemo: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val createMemo: StateFlow<UiState> = _createMemo

    fun saveMemo() {
        viewModelScope.launch {
            val startT = _eventStartTime.value!!
            val date = _eventDate.value!!
            checkIfDateIsPast(date, startT)
            if (eventIsValid.value == true) {
                try {
                    repository.createNewMemo(
                        Memo(
                            name = _eventName.value!!,
                            desc = eventDesc.value ?: "",
                            startTime = Utils.getDateFromDateTime(_eventDate.value!!, _eventStartTime.value!!)?.time ?: 0,
                            endTime = Utils.getDateFromDateTime(_eventDate.value!!, _eventEndTime.value!!)?.time ?: 0,
                        )
                    )
                    _createMemo.value = UiState.Success(null)
                } catch (e : Exception) {
                    _createMemo.value = UiState.Failure(e)
                }
            }
        }
    }

    fun setPickedDate(date: Long) {
        viewModelScope.launch {
            _eventDate.value = Utils.getFullDateFromTS(date)
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
        viewModelScope.launch {
            val startT = _eventStartTime.value!!
            val endT = _eventEndTime.value!!
            val date = _eventDate.value!!
            _eventIsValid.value = _eventName.value!!.isNotBlank() && date.isNotBlank() &&
                    startT.isNotBlank() && endT.isNotBlank()
            if (startT isAfter endT) {
                _eventIsValid.value = false
                endTimeError.value =
                    getApplication<Application?>().getString(R.string.err_end_time_error)
            } else {
                endTimeError.value = ""
            }
            checkIfDateIsPast(date, startT)
        }
    }

    private fun checkIfDateIsPast(date: String, startT: String) {
        if (Utils.isPast(date, startT)) {
            _eventIsValid.value = false
            startTimeError.value =
                getApplication<Application?>().getString(R.string.err_start_time_error)
        } else {
            startTimeError.value = ""
        }
    }

    fun resetUiState() {
        _createMemo.value = UiState.Idle
    }
}