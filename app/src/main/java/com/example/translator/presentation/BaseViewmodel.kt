package com.example.translator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewmodel : ViewModel() {
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { context, exception ->
            // Handle error here
        }

    fun launchTask(task: () -> Unit): Job {
        return viewModelScope.launch(coroutineExceptionHandler) {
            task.invoke()
        }
    }
}
