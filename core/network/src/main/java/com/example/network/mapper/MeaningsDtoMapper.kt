package com.example.network.mapper

import com.example.model.Meanings
import com.example.network.dto.MeaningsDto

fun MeaningsDto.toDomain(): Meanings {
    return Meanings(
        partOfSpeech = partOfSpeech,
        definitions = definitions.map { it.toDomain() },
    )
}
