package com.example.translator.presentation

import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.GetThemeUseCase
import com.example.ui.base.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewmodel
    @Inject
    constructor(
        private val getThemeUseCase: GetThemeUseCase,
    ) :
    BaseViewmodel() {
        private val _themeFlow = MutableStateFlow<Boolean?>(null)
        val themeFlow: StateFlow<Boolean?> = _themeFlow.asStateFlow()

        fun observeTheme() {
            getThemeUseCase.invoke(Unit).onEach {
                when (it) {
                    is UiState.Success -> _themeFlow.value = it.data
                    is UiState.Error -> failureState.value = it.error
                    UiState.Loading -> {
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
