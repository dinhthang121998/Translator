package com.example.translator.domain.model

data class Meanings(
    var partOfSpeech: String? = null,
    var definitions: ArrayList<Definitions> = arrayListOf(),
)
