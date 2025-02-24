package com.example.domain

import com.example.model.TranslatedWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateTranslatedFavoriteUseCase
    @Inject
    constructor(private val homeRepository: com.example.data.repository.HomeRepository) :
    BaseUseCase<TranslatedWord, Unit> {
        override fun invoke(param: TranslatedWord): Flow<Unit> =
            flow {
                homeRepository.updateTranslatedWordFavorite(param.id, param.isFavourite)
            }
    }
