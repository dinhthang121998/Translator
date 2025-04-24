package com.example.favored

import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.translationHistory.DeleteTranslationHistoryUseCase
import com.example.domain.translationHistory.GetFavoriteTranslationHistoryUseCase
import com.example.domain.translationHistory.UndoTranslationHistoryUseCase
import com.example.domain.translationHistory.UpdateFavoriteTranslationHistoryUseCase
import com.example.model.TranslationHistory
import com.example.ui.base.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoredViewmodel
    @Inject
    constructor(
        private val getFavoriteTranslationHistoryUseCase: GetFavoriteTranslationHistoryUseCase,
        private val deleteTranslationHistoryUseCase: DeleteTranslationHistoryUseCase,
        private val undoTranslationHistoryUseCase: UndoTranslationHistoryUseCase,
        private val updateFavoriteTranslationHistoryUseCase: UpdateFavoriteTranslationHistoryUseCase,
    ) : BaseViewmodel() {
        private val _getFavoriteTranslationHistoryFlow = MutableStateFlow(listOf<TranslationHistory>())
        val getFavoriteTranslationHistoryFlow = _getFavoriteTranslationHistoryFlow

        fun getFavoriteTranslationHistory() {
            getFavoriteTranslationHistoryUseCase.invoke(Unit).onEach { result ->
                when (result) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        result.data.let { translatedWords ->
                            _getFavoriteTranslationHistoryFlow.value = translatedWords
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun deleteTranslationHistory(id: Int) {
            viewModelScope.launch {
                deleteTranslationHistoryUseCase.invoke(id)
            }
        }

        fun undoTranslationHistory(translationHistory: TranslationHistory) {
            viewModelScope.launch {
                undoTranslationHistoryUseCase.invoke(translationHistory)
            }
        }

        fun updateTranslatedFavorite(translatedWord: TranslationHistory) {
            viewModelScope.launch {
                updateFavoriteTranslationHistoryUseCase.invoke(translatedWord)
            }
        }
    }
