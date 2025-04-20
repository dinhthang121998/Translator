package com.example.network.mapper

import com.example.model.WordInformation
import com.example.network.dto.WordInformationDto

fun WordInformationDto.toDomain(): WordInformation {
    return WordInformation(
        word = word,
        phonetic = phonetic,
        meanings = meanings.map { it.toDomain() },
    )
}
