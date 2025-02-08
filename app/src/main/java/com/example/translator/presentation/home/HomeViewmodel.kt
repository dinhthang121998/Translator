package com.example.translator.presentation.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.translator.common.UiState
import com.example.translator.domain.model.TranslatedWord
import com.example.translator.domain.usecase.HomeUseCase
import com.example.translator.presentation.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(private val homeUseCase: HomeUseCase) : BaseViewmodel() {

    private val _listTranslatedWord = MutableStateFlow(TranslatedWord())
    val listTranslatedWord = _listTranslatedWord

    fun addTranslatedWord(originalWord: String, translatedWord: String){
        val translated = TranslatedWord(originalWord, translatedWord, false)

        homeUseCase.addTranslatedWord(translated).onEach {
            when (it) {
                is UiState.Error -> {
                    Log.d("AAAA", "error = ${it.message}")
                }
                is UiState.Loading -> {
                    Log.d("AAAA", "loading")
                }
                is UiState.Success -> {
                    Log.d("AAAA", "success")
                }
            }
        }.launchIn(viewModelScope)
    }

}