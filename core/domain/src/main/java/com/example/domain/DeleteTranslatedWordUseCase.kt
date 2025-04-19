package com.example.domain

import com.example.data.repository.HomeRepository
import com.example.database.model.TranslatedEntity
import com.example.model.TranslatedWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteTranslatedWordUseCase @Inject constructor(
    private val homeRepository: HomeRepository
): BaseUseCase<TranslatedWord, Unit> {
    override fun invoke(param: TranslatedWord): Flow<Unit> = flow {
        homeRepository.deleteTranslatedWord(param.toTranslatedEntity())
    }

    private fun TranslatedWord.toTranslatedEntity(): TranslatedEntity {
        return TranslatedEntity(
            id = this.id,
            originalWord = this.originalWord,
            translatedWord = this.translatedWord,
            isFavourite = this.isFavourite,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
    }
}