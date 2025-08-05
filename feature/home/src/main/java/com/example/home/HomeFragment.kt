package com.example.home

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
import com.example.home.databinding.FragmentHomeBinding
import com.example.home.meaning.MeaningsAdapter
import com.example.model.MeaningItem
import com.example.model.Meanings
import com.example.model.SearchLanguageItem
import com.example.model.TranslationHistory
import com.example.model.WordInformation
import com.example.navigation.NavigateTranslateCamera
import com.example.navigation.NavigateTranslateImage
import com.example.ui.R
import com.example.ui.base.BaseFragment
import com.example.ui.translationHistory.TranslationHistoryAdapter
import com.example.ui.util.showOrGone
import com.example.ui.util.speechIntent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewmodel>(),
    TranslationHistoryAdapter.IHistoryTranslationAdapterListener {
    @Inject
    lateinit var navigateTranslateCamera: NavigateTranslateCamera

    @Inject
    lateinit var navigateTranslateImage: NavigateTranslateImage

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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observePairLanguageItemChange()
        viewModel.getTranslatedWords()

        setupOnClickView()
        setupHomeChooseLanguageView()
        setupOriginalView()
        setupTranslatedView()
        setupMeaningAdapter(listOf())
        setupHistoryTranslation()

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

        // Launch coroutine collectors as separate setup functions for clarity
        setupPairLanguageCollector()
        setupTranslatedTextCollector()
        setupWordDefinitionCollector()
        setupTranslatedWordsCollector()
        setupLoadingCollector()
        setupFailureCollector()
    }

    private fun setupHistoryTranslation() {
        binding.historyTranslation.iHistoryTranslationAdapter = this
    }

    private fun handleWordDefinition(wordDefinition: WordInformation) {
        handlePhoneticDisplay(wordDefinition.phonetic)

        handleSpeakOriginal(wordDefinition.word)
        handleSpeakTranslated(viewModel.translatedText)

        if (wordDefinition.word != null) {
            isShowMeaning(true)
            isShowHistoryTranslation(false)
        }

        handleMeaningDisplay(wordDefinition.meanings)
    }

    private fun handleMeaningDisplay(meanings: List<Meanings>) {
        val meaningItem = mutableListOf<MeaningItem>()
        meanings.forEach { meaning ->
            meaning.partOfSpeech?.let {
                meaningItem.add(MeaningItem.PartOfSpeechItem(it))
            }

            meaning.definitions.forEach {
                meaningItem.add(MeaningItem.DefinitionsItem(it))
            }
        }
        meaningsAdapter?.updateListMeanings(meaningItem)
    }

    private fun handleSpeakTranslated(translatedText: String) {
        val isShowSpeakTranslated = translatedText.isNotEmpty()
        binding.translated.showSpeak(isShowSpeakTranslated)
    }

    private fun handleSpeakOriginal(word: String?) {
        val isShowSpeak = word != null
        binding.original.showSpeak(isShowSpeak)
    }

    private fun handlePhoneticDisplay(phonetic: String?) {
        phonetic?.let {
            binding.original.showPhonetic(true)
            binding.original.setPhonetic(phonetic)
        } ?: run {
            binding.original.showPhonetic(false)
        }
    }

    private fun handlePairLanguage(
        fromLanguageItem: SearchLanguageItem.LanguageItem,
        toLanguageItem: SearchLanguageItem.LanguageItem,
    ) {
        setFromLanguage(fromLanguageItem)
        setToLanguage(toLanguageItem)

        translatedIfAvailable(fromLanguageItem, toLanguageItem)
    }

    private fun translatedIfAvailable(
        fromLanguageItem: SearchLanguageItem.LanguageItem,
        toLanguageItem: SearchLanguageItem.LanguageItem,
    ) {
        if (viewModel.originalText.isNotEmpty() && fromLanguageItem.languageCode.isNotEmpty() &&
            toLanguageItem.languageCode.isNotEmpty()
        ) {
            viewModel.translate(
                viewModel.originalText,
                fromLanguageItem.languageCode,
                toLanguageItem.languageCode,
            )
        }
    }

    private fun setToLanguage(toLanguageItem: SearchLanguageItem.LanguageItem) {
        val toLanguage =
            toLanguageItem.languageName.ifEmpty {
                getString(R.string.search)
            }
        binding.homeSelectLanguage.setToLanguage(toLanguage)
    }

    private fun setFromLanguage(fromLanguageItem: SearchLanguageItem.LanguageItem) {
        val fromLanguage =
            fromLanguageItem.languageName.ifEmpty {
                getString(R.string.search)
            }
        binding.homeSelectLanguage.setFromLanguage(fromLanguage)
    }

    private fun setupHomeChooseLanguageView() {
        binding.homeSelectLanguage.apply {
            onClickFromLanguage = {
                showSearchBottomSheet(clickItem = { searchLanguageItem ->
                    val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                    setFromLanguage(languageItem.languageName)
                    viewModel.storeLanguageItem(true, languageItem)
                })
            }

            onClickToLanguage = {
                showSearchBottomSheet(clickItem = { searchLanguageItem ->
                    val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                    setToLanguage(languageItem.languageName)
                    viewModel.storeLanguageItem(false, languageItem)
                })
            }

            onClickSwitch = {
            }
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
            viewModel.getWordDefinition(viewModel.originalText)
            viewModel.addTranslationHistory(viewModel.originalText, viewModel.translatedText)
        }
        binding.translated.onClickSpeak = { text ->
            val pairLanguageItem = viewModel.pairLanguageFlow.value
            viewModel.speak(text, pairLanguageItem.second.languageCode)
        }
    }

    private fun setupOriginalView() {
        binding.original.showMic(true)
        binding.original.onClickMic = {
            val pairLanguageItem = viewModel.pairLanguageFlow.value
            val intent = speechIntent(pairLanguageItem.first.languageCode)
            speechResultLauncher.launch(intent)
        }

        binding.original.onFocusListener = { hasFocus ->
            val visibility = if (hasFocus) View.VISIBLE else View.GONE
            binding.cTranslated.visibility = visibility
        }
        binding.original.onTextChanged = { originalText ->
            val pairLanguageItem = viewModel.pairLanguageFlow.value
            viewModel.originalText = originalText
            viewModel.translate(
                originalText,
                pairLanguageItem.first.languageCode,
                pairLanguageItem.second.languageCode,
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
            val pairLanguageItem = viewModel.pairLanguageFlow.value
            viewModel.speak(text, pairLanguageItem.first.languageCode)
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

    private fun isShowMeaning(isShow: Boolean) {
        binding.rcvMeaning.showOrGone(isShow)
    }

    private fun isShowHistoryTranslation(isShow: Boolean) {
        binding.historyTranslation.showOrGone(isShow)
    }

    private fun resetAll() {
        resetText()
        hidePhonetic()
        hideSpeak()
        isShowMeaning(false)
        isShowHistoryTranslation(true)

        viewModel.originalText = ""
        viewModel.translatedText = ""
    }

    private fun setupOnClickView() {
        binding.ivImage.setOnClickListener {
            navigateTranslateImage.navigateToTranslateImage(requireContext())
        }

        binding.ivCamera.setOnClickListener {
            navigateTranslateCamera.navigateToTranslateCamera(requireContext())
        }
    }

    /** Collects pairLanguageFlow and handles language pair selection UI and logic. */
    private fun setupPairLanguageCollector() {
        lifecycleScope.launch {
            viewModel.pairLanguageFlow.collect { (fromLanguageItem, toLanguageItem) ->
                handlePairLanguage(fromLanguageItem, toLanguageItem)
            }
        }
    }

    /** Collects translatedTextFlow and updates the translated UI field. */
    private fun setupTranslatedTextCollector() {
        lifecycleScope.launch {
            viewModel.translatedTextFlow.collect { translatedText ->
                binding.translated.setText(translatedText)
            }
        }
    }

    /** Collects textDefinitionFlow and updates word definition UI. */
    private fun setupWordDefinitionCollector() {
        lifecycleScope.launch {
            viewModel.textDefinitionFlow.collect { wordDefinition ->
                handleWordDefinition(wordDefinition)
            }
        }
    }

    /** Collects getTranslatedWordsFlow and updates translation history. */
    private fun setupTranslatedWordsCollector() {
        lifecycleScope.launch {
            viewModel.getTranslatedWordsFlow.collect { translatedWords ->
                binding.historyTranslation.updateTranslationHistory(translatedWords)
            }
        }
    }

    /** Collects loadingFlow and shows/hides loading overlay. */
    private fun setupLoadingCollector() {
        lifecycleScope.launch {
            viewModel.loadingFlow.collect { isLoading ->
                binding.loadingOverlay.showOrGone(isLoading)
            }
        }
    }

    /** Collects failureFlow and shows errors via AlertDialog. */
    private fun setupFailureCollector() {
        lifecycleScope.launch {
            viewModel.failureFlow.collect { exception ->
                exception?.let {
                    showAlertDialog(
                        requireContext(),
                        getString(R.string.error),
                        "${exception.message}",
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }

    override fun onClickFavorite(translatedWord: TranslationHistory) {
        viewModel.updateTranslatedFavorite(translatedWord)
    }

    override fun onClickItem(translatedWord: TranslationHistory) {
    }

    override fun onDeleteHistoryItem(translatedWord: TranslationHistory) {
        viewModel.deleteTranslationHistory(translatedWord.id)
        view?.let { view ->
            Snackbar.make(
                view,
                getString(R.string.item_is_deleted),
                Snackbar.LENGTH_LONG,
            ).setAction(getString(R.string.undo)) {
                // Undo the deletion
                viewModel.undoTranslationHistory(
                    translatedWord,
                )
            }.show()
        }
    }
}
