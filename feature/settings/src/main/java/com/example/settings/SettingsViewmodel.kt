package com.example.settings

import androidx.lifecycle.viewModelScope
import com.example.domain.translationHistory.DeleteAllTranslationHistoryUseCase
import com.example.ui.base.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel
    @Inject
    constructor(
        private val deleteAllTranslationHistoryUseCase: DeleteAllTranslationHistoryUseCase,
    ) :
    BaseViewmodel() {
        fun clearAllTranslationHistory() {
            viewModelScope.launch {
                deleteAllTranslationHistoryUseCase.invoke(Unit)
            }
        }
    }
