package com.example.network.dto

import com.google.gson.annotations.SerializedName

data class MeaningsDto(
    @SerializedName("partOfSpeech") var partOfSpeech: String? = null,
    @SerializedName("definitions") var definitions: ArrayList<DefinitionsDto> = arrayListOf(),
)
