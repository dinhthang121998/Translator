package com.example.translator.domain.usecase

import com.example.translator.data.mapper.toTranslatedEntity
import com.example.translator.domain.model.TranslatedWord
import com.example.translator.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddTranslatedWordUseCase
    @Inject
    constructor(private val homeRepository: HomeRepository) :
    BaseUseCase<TranslatedWord, Unit> {
        override fun invoke(param: TranslatedWord): Flow<Unit> =
            flow {
                val translatedEntity = param.toTranslatedEntity()
                homeRepository.addTranslatedWord(translatedEntity)
            }
    }
