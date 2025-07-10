package com.example.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewmodel : ViewModel() {
    // failure here
    protected val failureState = MutableStateFlow<Throwable?>(null)
    val failureFlow: StateFlow<Throwable?> = failureState.asStateFlow()

    // loading here
    protected val loadingState = MutableStateFlow(false)
    val loadingFlow: StateFlow<Boolean> = loadingState.asStateFlow()
}
