package com.example.translator.domain.usecase

import com.example.translator.common.UiState
import com.example.translator.data.mapper.toTranslatedWord
import com.example.translator.domain.model.TranslatedWord
import com.example.translator.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTranslatedWordUseCase
    @Inject
    constructor(private val homeRepository: HomeRepository) :
    BaseUseCase<Unit, UiState<List<TranslatedWord>>> {
        override fun invoke(param: Unit): Flow<UiState<List<TranslatedWord>>> =
            homeRepository.getAllTranslatedWord()
                .map { listEntity -> UiState.Success(listEntity.map { translatedEntity -> translatedEntity.toTranslatedWord() }) }
    }
