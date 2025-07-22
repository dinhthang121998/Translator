package com.example.translator.presentation

import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.GetThemeUseCase
import com.example.ui.base.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
        private val _themeSharedFlow = MutableSharedFlow<Boolean>()
        val themeSharedFlow: SharedFlow<Boolean> = _themeSharedFlow.asSharedFlow()

        fun observeTheme() {
            getThemeUseCase.invoke(Unit).onEach {
                when (it) {
                    is UiState.Success -> {
                        it.data?.let { isDarkTheme ->
                            _themeSharedFlow.emit(isDarkTheme)
                        }
                    }

                    is UiState.Error -> failureState.value = it.error
                    UiState.Loading -> {
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
