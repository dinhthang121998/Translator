package com.example.domain

import com.example.common.UiState
import com.example.data.repository.HomeRepository
import com.example.domain.base.FlowUseCase
import com.example.model.TranslatedWord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTranslatedWordUseCase
    @Inject
    constructor(private val homeRepository: HomeRepository, coroutineDispatcher: CoroutineDispatcher) :
    FlowUseCase<Unit, List<TranslatedWord>>(coroutineDispatcher) {
        override fun execute(parameter: Unit): Flow<UiState<List<TranslatedWord>>> {
            return homeRepository.getAllTranslatedWord()
                .map { listEntity -> UiState.Success(listEntity) }
        }
    }
