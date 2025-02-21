package com.example.translator.presentation.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translator.R
import com.example.translator.databinding.FragmentHomeBinding
import com.example.translator.domain.model.MeaningItem
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.domain.model.TranslatedWord
import com.example.translator.presentation.BaseFragment
import com.example.translator.presentation.home.meaning.MeaningsAdapter
import com.example.translator.presentation.home.searchLanguage.bottomSheet.SearchSelectedLanguageSheet
import com.example.translator.presentation.home.translatedWord.TranslatedWordAdapter
import com.example.translator.presentation.translateImage.TranslateImageActivity
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

    // Declare an ActivityResultLauncher
    private lateinit var speechResultLauncher: ActivityResultLauncher<Intent>
    private var meaningsAdapter: MeaningsAdapter? = null
    private var translatedWordAdapter: TranslatedWordAdapter? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observePairLanguageItemChange()
        viewModel.getTranslatedWords()

        setupOnClickView()
        setupOriginalView()
        setupTranslatedView()
        setupMeaningAdapter(listOf())
        setupTranslatedWordAdapter(listOf())

        speechResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val results =
                        result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    results?.firstOrNull()?.let {
                        Log.d("SpeechToText", "Recognized Text: $it")
                    }
                }
            }

        lifecycleScope.launch {
            launch {
                viewModel.pairLanguageFlow.collect { (fromLanguageItem, toLanguageItem) ->
                    if (fromLanguageItem.languageName.isEmpty()) {
                        binding.tvFromLanguage.text = getString(R.string.search)
                    } else {
                        viewModel.fromLanguageItem = fromLanguageItem
                        binding.tvFromLanguage.text = fromLanguageItem.languageName
                    }

                    if (fromLanguageItem.languageName.isEmpty()) {
                        binding.tvToLanguage.text = getString(R.string.search)
                    } else {
                        viewModel.toLanguageItem = toLanguageItem
                        binding.tvToLanguage.text = toLanguageItem.languageName
                    }

                    if (viewModel.originalText.isNotEmpty() && viewModel.fromLanguageItem.languageCode.isNotEmpty() &&
                        viewModel.toLanguageItem.languageCode.isNotEmpty()
                    ) {
                        viewModel.translate(
                            viewModel.originalText,
                            viewModel.fromLanguageItem.languageCode,
                            viewModel.toLanguageItem.languageCode,
                        )
                    }
                }
            }

            launch {
                viewModel.translatedTextFlow.collect { translatedText ->
                    binding.translated.setText(translatedText)
                }
            }

            launch {
                viewModel.textDefinitionFlow.collect { wordDefinition ->
                    wordDefinition.phonetic?.let { phonetic ->
                        binding.original.showPhonetic(true)
                        binding.original.setPhonetic(phonetic)
                    } ?: run {
                        binding.original.showPhonetic(false)
                    }

                    val isShowSpeak = wordDefinition.word != null
                    binding.original.showSpeak(isShowSpeak)

                    val isShowSpeakTranslated = viewModel.translatedText.isNotEmpty()
                    binding.translated.showSpeak(isShowSpeakTranslated)
                }
            }

            launch {
                viewModel.getTranslatedWordsFlow.collect { translatedWords ->
                    translatedWordAdapter?.updateTranslatedWords(translatedWords)
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

    private fun setupTranslatedWordAdapter(translatedWords: List<TranslatedWord>) {
        translatedWordAdapter =
            TranslatedWordAdapter(translatedWords, onClickFavorite = { translatedItem ->
                viewModel.updateTranslatedFavorite(translatedItem)
            }, onClickItem = {})
        binding.rcvTranslatedWord.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.requireContext())
            adapter = translatedWordAdapter
        }
    }

    private fun setupMeaningAdapter(meaningsItem: List<MeaningItem>) {
        meaningsAdapter = MeaningsAdapter(meaningsItem)
        binding.rcvMeaning.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.requireContext())
            adapter = meaningsAdapter
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
            if (viewModel.fromLanguageItem.languageCode == "en") {
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
            // TODO Implements Mic
            val intent =
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                    )
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE,
                        viewModel.fromLanguageItem.languageCode,
                    ) // Set the language
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
                }

            speechResultLauncher.launch(intent)

            // TODO: Not tested, will test later
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

            hideSpeak()
            hidePhonetic()
        }
        binding.original.onClickClose = {
            resetAll()
        }

        binding.original.onClickSpeak = { text ->
            viewModel.speak(text, viewModel.fromLanguageItem.languageCode)
        }
    }

    private fun resetText() {
        binding.original.setText("")
        binding.original.setPhonetic("")
        binding.translated.setPhonetic("")
    }

    private fun hidePhonetic() {
        binding.original.showPhonetic(false)
        binding.translated.showPhonetic(false)
    }

    private fun hideSpeak() {
        binding.original.showSpeak(false)
        binding.translated.showSpeak(false)
    }

    private fun resetAll() {
        resetText()
        hidePhonetic()
        hideSpeak()

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

        binding.ivImage.setOnClickListener {
            val intent = Intent(context, TranslateImageActivity::class.java)
            startActivity(intent)
        }

        binding.ivCamera.setOnClickListener {

        }
    }

    private fun showSearchBottomSheet(clickItem: ((SearchLanguageItem) -> Unit)? = null) {
        val bottomSheetLanguage =
            SearchSelectedLanguageSheet.newInstance().apply {
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
