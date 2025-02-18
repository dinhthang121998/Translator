package com.example.translator.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PhoneticsDto(
    @SerializedName("text") var text: String? = null,
    @SerializedName("audio") var audio: String? = null,
)
