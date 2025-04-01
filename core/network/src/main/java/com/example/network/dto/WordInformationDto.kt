package com.example.network.dto

import com.google.gson.annotations.SerializedName

data class WordInformationDto(
    @SerializedName("word") var word: String? = null,
    @SerializedName("phonetic") var phonetic: String? = null,
    @SerializedName("phonetics") var phonetics: ArrayList<PhoneticsDto> = arrayListOf(),
    @SerializedName("origin") var origin: String? = null,
    @SerializedName("meanings") var meanings: ArrayList<MeaningsDto> = arrayListOf(),
)
