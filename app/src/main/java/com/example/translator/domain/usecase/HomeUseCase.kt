package com.example.translator.domain.usecase

import com.example.translator.common.UiState
import com.example.translator.data.mapper.toTranslatedEntity
import com.example.translator.domain.model.TranslatedWord
import com.example.translator.domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeUseCase
    @Inject
    constructor(
        private val homeRepository: HomeRepository,
    ) {
        fun addTranslatedWord(translatedWord: TranslatedWord): Flow<UiState<Nothing>> =
            flow {
                emit(UiState.Loading())
                try {
                    val translatedEntity = translatedWord.toTranslatedEntity()
                    homeRepository.addTranslatedWord(translatedEntity)
                    emit(UiState.Success(data = null))
                } catch (e: Exception) {
                    emit(UiState.Error(message = e.message.toString()))
                }
            }.flowOn(Dispatchers.IO)
    }
