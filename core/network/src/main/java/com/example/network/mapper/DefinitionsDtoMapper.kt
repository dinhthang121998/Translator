package com.example.network.mapper

import com.example.model.Definitions
import com.example.network.dto.DefinitionsDto

fun DefinitionsDto.toDomain(): Definitions {
    return Definitions(definition, example, synonyms, antonyms)
}
