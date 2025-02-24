package com.example.model

data class Definitions(
    var definition: String? = null,
    var example: String? = null,
    var synonyms: ArrayList<String> = arrayListOf(),
    var antonyms: ArrayList<String> = arrayListOf(),
)
