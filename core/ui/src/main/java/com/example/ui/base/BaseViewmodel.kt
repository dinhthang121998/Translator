package com.example.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewmodel : ViewModel() {
    // failure here
    protected val failureState = MutableStateFlow<Throwable?>(null)
    val failureFlow: StateFlow<Throwable?> = failureState.asStateFlow()

    // loading here
    protected val loadingState = MutableStateFlow(false)
    val loadingFlow: StateFlow<Boolean> = loadingState.asStateFlow()
}
