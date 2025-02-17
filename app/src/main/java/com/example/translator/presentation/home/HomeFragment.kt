package com.example.translator.presentation.home

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.translator.LanguageItem
import com.example.translator.R
import com.example.translator.databinding.FragmentHomeBinding
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.presentation.BaseFragment
import com.example.translator.presentation.home.searchLanguage.bottomSheet.SearchSelectedLanguageSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewmodel>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: HomeViewmodel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observePairLanguageItemChange()

        setupOnClickView()
        setupOriginalView()
        setupTranslatedView()

        lifecycleScope.launch {
            launch {
                viewModel.pairLanguage.collect { (fromLanguageItem, toLanguageItem) ->
                    fromLanguageItem?.let {
                        viewModel.fromLanguageItem = fromLanguageItem
                        binding.tvFromLanguage.text = fromLanguageItem.languageName
                    } ?: run { binding.tvFromLanguage.text = getString(R.string.search) }

                    toLanguageItem?.let {
                        viewModel.toLanguageItem = toLanguageItem
                        binding.tvToLanguage.text = toLanguageItem.languageName
                    } ?: run { binding.tvToLanguage.text = getString(R.string.search) }

                    if (viewModel.originalText.isNotEmpty() && viewModel.fromLanguageItem.languageCode.isNotEmpty() && viewModel.toLanguageItem.languageCode.isNotEmpty()) {
                        viewModel.translate(
                            viewModel.originalText,
                            viewModel.fromLanguageItem.languageCode,
                            viewModel.toLanguageItem.languageCode
                        )
                    }
                }
            }

            launch {
                viewModel.translatedTextState.collect { translatedText ->
                    binding.translated.setText(translatedText)
                }
            }

            launch {
                viewModel.textDefinitionState.collect { wordDefinition ->
                    wordDefinition.phonetic?.let { phonetic ->
                        binding.original.showPhonetic(true)
                        binding.original.setPhonetic(phonetic)
                    } ?: run {
                        binding.original.showPhonetic(false)
                    }

                    val isShowSpeak = wordDefinition.word != null
                    Log.d("AAAA", "isShowSpeak = $isShowSpeak")
                    binding.original.showSpeak(isShowSpeak)

                    val isShowSpeakTranslated = viewModel.translatedText.isNotEmpty()
                    Log.d("AAAA", "isShowSpeakTranslated = $isShowSpeakTranslated")
                    binding.translated.showSpeak(isShowSpeakTranslated)
                }
            }

//            launch {
//                viewModel.swapTextState.collect { (fromText, toText) ->
//                    viewModel.originalText = fromText
//                    viewModel.translatedText = toText
//                    binding.original.setText(fromText)
//                    binding.translated.setText(toText)
//                }
//            }
        }
    }

    private fun setupTranslatedView() {
        binding.translated.setReadOnly(true)
        binding.translated.setUpTranslatedView()
        binding.translated.showMic(false)

        binding.translated.onTextChanged = { translatedText ->
            viewModel.translatedText = translatedText
        }
        binding.translated.onClickTranslation = {
            if (viewModel.fromLanguageItem.languageCode == "en"){
                viewModel.getWordDefinition(viewModel.originalText)
            }
            viewModel.addTranslatedWord(viewModel.originalText, viewModel.translatedText)
        }
        binding.translated.onClickSpeak = { text ->
            viewModel
        }
    }

    private fun setupOriginalView() {
        binding.original.showMic(true)
        binding.original.onClickMic = {
            //
        }

        binding.original.onFocusListener = { hasFocus ->
            val visibility = if (hasFocus) View.VISIBLE else View.GONE
            binding.cTranslated.visibility = visibility
        }
        binding.original.onTextChanged = { originalText ->
            viewModel.originalText = originalText
            viewModel.translate(
                originalText,
                viewModel.fromLanguageItem.languageCode,
                viewModel.toLanguageItem.languageCode,
            )
            val isShowed = originalText.isNotEmpty()
            binding.original.showClearIcon(isShowed)
            binding.original.showMic(!isShowed)
        }
        binding.original.onClickClose = {
            resetAll()
        }

        binding.original.onClickSpeak = { text ->
            viewModel.speak(text, viewModel.fromLanguageItem.languageCode)
        }
    }

    private fun resetAll() {
        binding.original.setText("")
        binding.original.setPhonetic("")
        binding.original.showSpeak(false)
        binding.original.showPhonetic(false)

        binding.translated.setPhonetic("")
        binding.translated.showPhonetic(false)
        binding.translated.showSpeak(false)

        viewModel.originalText = ""
        viewModel.translatedText = ""
    }

    private fun setupOnClickView() {
        binding.tvFromLanguage.setOnClickListener {
            showSearchBottomSheet { searchLanguageItem ->
                val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                binding.tvFromLanguage.text = languageItem.languageName
                viewModel.storeLanguageItem(true, languageItem)
            }
        }

        binding.tvToLanguage.setOnClickListener {
            showSearchBottomSheet { searchLanguageItem ->
                val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                binding.tvToLanguage.text = languageItem.languageName
                viewModel.storeLanguageItem(false, languageItem)
            }
        }

        binding.ivSwitch.setOnClickListener {
            viewModel.swapLanguageItem(viewModel.fromLanguageItem, viewModel.toLanguageItem)
        }
    }

    private fun showSearchBottomSheet(clickItem: ((SearchLanguageItem) -> Unit)? = null) {
        val bottomSheetLanguage = SearchSelectedLanguageSheet.newInstance().apply {
            clickCloseButton = {
                this.dismiss()
            }
            clickItemButton = { searchLanguageItem: SearchLanguageItem ->
                val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                clickItem?.invoke(languageItem)
                this.dismiss()
            }
        }
        bottomSheetLanguage.show(childFragmentManager, SearchSelectedLanguageSheet.TAG)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
