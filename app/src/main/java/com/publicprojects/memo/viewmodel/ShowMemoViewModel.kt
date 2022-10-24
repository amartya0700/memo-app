package com.publicprojects.memo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.publicprojects.memo.model.repository.MemoRepository
import com.publicprojects.memo.view.sealed.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowMemoViewModel @Inject constructor(
    private val repository: MemoRepository
): ViewModel() {

    private val _showMemo: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val showMemo: StateFlow<UiState> = _showMemo

    fun getLatestMemos() {
        viewModelScope.launch {
            try {
                _showMemo.value = UiState.Success(repository.fetchUpcomingMemos())
            } catch (e: Exception) {
                _showMemo.value = UiState.Failure(e)
            }
        }
    }

    fun resetUiState() {
        _showMemo.value = UiState.Idle
    }
}