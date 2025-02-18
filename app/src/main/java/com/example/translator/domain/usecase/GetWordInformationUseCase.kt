package com.example.translator.domain.usecase

import com.example.translator.common.UiState
import com.example.translator.data.mapper.toWordInformation
import com.example.translator.domain.model.WordInformation
import com.example.translator.domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetWordInformationUseCase
    @Inject
    constructor(private val homeRepository: HomeRepository) :
    BaseUseCase<String, UiState<WordInformation>> {
        override fun invoke(param: String): Flow<UiState<WordInformation>> =
            flow {
                emit(UiState.Loading())
                try {
                    val wordInformationDto = homeRepository.getWordInformation(param)
                    emit(UiState.Success(data = wordInformationDto[0].toWordInformation()))
                } catch (e: Exception) {
                    emit(UiState.Error(message = e.message.toString()))
                }
            }.flowOn(Dispatchers.IO)
    }
