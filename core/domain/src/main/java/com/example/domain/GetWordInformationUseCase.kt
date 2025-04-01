package com.example.domain

import com.example.common.UiState
import com.example.model.Definitions
import com.example.model.Meanings
import com.example.model.WordInformation
import com.example.network.dto.DefinitionsDto
import com.example.network.dto.MeaningsDto
import com.example.network.dto.WordInformationDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetWordInformationUseCase
    @Inject
    constructor(private val homeRepository: com.example.data.repository.HomeRepository) :
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

        private fun ArrayList<DefinitionsDto>.toDefinitions(): ArrayList<Definitions> {
            return this.map { definition ->
                Definitions(
                    definition.definition,
                    definition.example,
                    definition.synonyms,
                    definition.antonyms,
                )
            } as ArrayList<Definitions>
        }

        private fun ArrayList<MeaningsDto>.toMeanings(): ArrayList<Meanings> {
            return this.map { meaning ->
                Meanings(meaning.partOfSpeech, meaning.definitions.toDefinitions())
            } as ArrayList<Meanings>
        }

        private fun WordInformationDto.toWordInformation() =
            WordInformation(
                word = this.word,
                phonetic = this.phonetic,
                meaning = this.meanings.toMeanings(),
            )
    }
