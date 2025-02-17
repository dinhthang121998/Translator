package com.example.translator.domain.model

import com.example.translator.data.remote.dto.DefinitionsDto
import com.google.gson.annotations.SerializedName

data class Meanings(
    var partOfSpeech: String? = null,
    var definitions: ArrayList<Definitions> = arrayListOf()
)