package com.example.model

data class Meanings(
    var partOfSpeech: String? = null,
    var definitions: ArrayList<Definitions> = arrayListOf(),
)
