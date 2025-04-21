package com.example.domain

import com.example.data.repository.WordInformationRepository
import com.example.domain.base.SuspendUseCase
import com.example.model.WordInformation
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetWordInformationUseCase
    @Inject
    constructor(private val wordInformationRepository: WordInformationRepository, coroutineDispatcher: CoroutineDispatcher) :
    SuspendUseCase<String, WordInformation>(coroutineDispatcher) {
        override suspend fun execute(parameter: String): WordInformation {
            return wordInformationRepository.getWordInformation(parameter)[0]
        }
    }
