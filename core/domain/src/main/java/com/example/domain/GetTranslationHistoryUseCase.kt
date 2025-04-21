package com.example.domain

import com.example.common.UiState
import com.example.data.repository.TranslationHistoryRepository
import com.example.domain.base.FlowUseCase
import com.example.model.TranslationHistory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTranslationHistoryUseCase
    @Inject
    constructor(private val translationHistoryRepository: TranslationHistoryRepository, coroutineDispatcher: CoroutineDispatcher) :
    FlowUseCase<Unit, List<TranslationHistory>>(coroutineDispatcher) {
        override fun execute(parameter: Unit): Flow<UiState<List<TranslationHistory>>> {
            return translationHistoryRepository.getAllTranslationHistory()
                .map { listEntity -> UiState.Success(listEntity) }
        }
    }
