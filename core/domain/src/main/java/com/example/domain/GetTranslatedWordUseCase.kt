package com.example.domain

import com.example.common.UiState
import com.example.database.model.TranslatedEntity
import com.example.model.TranslatedWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTranslatedWordUseCase
    @Inject
    constructor(private val homeRepository: com.example.data.repository.HomeRepository) :
    BaseUseCase<Unit, UiState<List<TranslatedWord>>> {
        override fun invoke(param: Unit): Flow<UiState<List<TranslatedWord>>> =
            homeRepository.getAllTranslatedWord()
                .map { listEntity -> UiState.Success(listEntity.map { translatedEntity -> translatedEntity.toTranslatedWord() }) }

        private fun TranslatedEntity.toTranslatedWord() = TranslatedWord(this.id, this.originalWord, this.translatedWord, this.isFavourite, this.createdAt, this.updatedAt)
    }
