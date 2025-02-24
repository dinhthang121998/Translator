package com.example.network.dto

import com.google.gson.annotations.SerializedName

data class DefinitionsDto(
    @SerializedName("definition") var definition: String? = null,
    @SerializedName("example") var example: String? = null,
    @SerializedName("synonyms") var synonyms: ArrayList<String> = arrayListOf(),
    @SerializedName("antonyms") var antonyms: ArrayList<String> = arrayListOf(),
)
