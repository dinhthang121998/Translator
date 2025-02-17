package com.example.translator.domain.model

data class WordInformation (
    var word: String? = null,
    var phonetic: String? = null,
    var meaning: ArrayList<Meanings> = arrayListOf()
)