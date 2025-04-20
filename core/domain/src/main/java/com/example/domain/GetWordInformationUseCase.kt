package com.example.domain

import com.example.data.repository.HomeRepository
import com.example.domain.base.SuspendUseCase
import com.example.model.WordInformation
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetWordInformationUseCase
    @Inject
    constructor(private val homeRepository: HomeRepository, coroutineDispatcher: CoroutineDispatcher) :
    SuspendUseCase<String, WordInformation>(coroutineDispatcher) {
        override suspend fun execute(parameter: String): WordInformation {
            return homeRepository.getWordInformation(parameter)[0]
        }
    }
