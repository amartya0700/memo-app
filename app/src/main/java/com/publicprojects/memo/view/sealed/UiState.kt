package com.publicprojects.memo.view.sealed

sealed class UiState {
    data class Success(val res: Any?): UiState()
    data class Failure(val t: Throwable): UiState()
    object Idle: UiState()
}
