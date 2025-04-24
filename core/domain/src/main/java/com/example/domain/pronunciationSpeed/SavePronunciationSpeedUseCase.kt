package com.example.domain.pronunciationSpeed

import com.example.data.repository.PronunciationSpeedRepository
import com.example.domain.base.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher

class SavePronunciationSpeedUseCase(
    private val pronunciationSpeedRepository: PronunciationSpeedRepository,
    coroutineDispatcher: CoroutineDispatcher,
) :
    SuspendUseCase<String, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: String) {
        pronunciationSpeedRepository.savePronunciationSpeed(parameter)
    }
}
