package com.example.domain.pronunciationSpeed

import com.example.common.UiState
import com.example.data.repository.PronunciationSpeedRepository
import com.example.domain.base.FlowUseCase
import com.example.model.PronunciationSpeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPronunciationSpeedUseCase(
    private val pronunciationSpeedRepository: PronunciationSpeedRepository,
    coroutineDispatcher: CoroutineDispatcher,
) :
    FlowUseCase<Unit, List<PronunciationSpeed>>(coroutineDispatcher) {
    override fun execute(parameter: Unit): Flow<UiState<List<PronunciationSpeed>>> {
        return pronunciationSpeedRepository.getPronunciationSpeedFlow().map { UiState.Success(it) }
    }
}
