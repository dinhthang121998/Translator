package com.example.pronunciationspeed

import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.pronunciationSpeed.GetPronunciationSpeedUseCase
import com.example.domain.pronunciationSpeed.SavePronunciationSpeedUseCase
import com.example.model.PronunciationSpeed
import com.example.ui.base.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PronunciationSpeedViewmodel
    @Inject
    constructor(
        private val savePronunciationSpeedUseCase: SavePronunciationSpeedUseCase,
        private val getPronunciationSpeedUseCase: GetPronunciationSpeedUseCase,
    ) : BaseViewmodel() {
        private val _pronunciationSpeed = MutableStateFlow<List<PronunciationSpeed>>(listOf())
        val pronunciationSpeed = _pronunciationSpeed

        fun savePronunciationSpeed(speed: PronunciationSpeed) {
            viewModelScope.launch {
                savePronunciationSpeedUseCase.invoke(speed.speedType.typeName)
            }
        }

        fun getPronunciationSpeed() {
            getPronunciationSpeedUseCase.invoke(Unit).onEach { result ->
                when (result) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        result.data.let { pairLanguage ->
                            _pronunciationSpeed.value = pairLanguage
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
