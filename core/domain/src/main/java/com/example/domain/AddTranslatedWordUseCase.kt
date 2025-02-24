package com.example.domain

import com.example.database.model.TranslatedEntity
import com.example.model.TranslatedWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddTranslatedWordUseCase
    @Inject
    constructor(private val homeRepository: com.example.data.repository.HomeRepository) :
    BaseUseCase<TranslatedWord, Unit> {
        override fun invoke(param: TranslatedWord): Flow<Unit> =
            flow {
                val translatedEntity = param.toTranslatedEntity()
                homeRepository.addTranslatedWord(translatedEntity)
            }

        fun TranslatedWord.toTranslatedEntity(): TranslatedEntity {
            return TranslatedEntity(
                originalWord = this.originalWord,
                translatedWord = this.translatedWord,
                isFavourite = this.isFavourite,
            )
        }
    }
