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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.FragmentHomeBinding
import com.example.home.meaning.MeaningsAdapter
import com.example.home.translatedWord.TranslatedWordAdapter
import com.example.model.MeaningItem
import com.example.model.Meanings
import com.example.model.SearchLanguageItem
import com.example.model.TranslatedWord
import com.example.model.WordInformation
import com.example.translatecamerax.TranslateCameraXActivity
import com.example.translateimage.TranslateImageActivity
import com.example.ui.R
import com.example.ui.base.BaseFragment
import com.example.ui.util.navigateToActivity
import com.example.ui.util.showOrGone
import com.example.ui.util.speechIntent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
        setUpHomeChooseLanguageView()
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
                // save to proto datastore
                viewModel.pairLanguageFlow.collect { (fromLanguageItem, toLanguageItem) ->
                    handlePairLanguage(fromLanguageItem, toLanguageItem)
                }
            }

            launch {
                viewModel.translatedTextFlow.collect { translatedText ->
                    binding.translated.setText(translatedText)
                }
            }

            launch {
                viewModel.textDefinitionFlow.collect { wordDefinition ->
                    // api response
                    handleWordDefinition(wordDefinition)
                }
            }

            launch {
                viewModel.getTranslatedWordsFlow.collect { translatedWords ->
                    // local db response
                    translatedWordAdapter?.updateTranslatedWords(translatedWords)
                }
            }

            launch {
                viewModel.loadingFlow.collect { isLoading ->
                    binding.loadingOverlay.showOrGone(isLoading)
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

    private fun handleWordDefinition(wordDefinition: WordInformation) {
        handlePhoneticDisplay(wordDefinition.phonetic)

        handleSpeakOriginal(wordDefinition.word)
        handleSpeakTranslated(viewModel.translatedText)

        if (wordDefinition.word != null) {
            isShowMeaning(true)
            isShowTranslatedWord(false)
        }

        handleMeaningDisplay(wordDefinition.meaning)
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

        translatedIfAvailable()
    }

    private fun translatedIfAvailable() {
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

    private fun setToLanguage(toLanguageItem: SearchLanguageItem.LanguageItem) {
        val toLanguage =
            if (toLanguageItem.languageName.isEmpty()) {
                getString(R.string.search)
            } else {
                viewModel.toLanguageItem = toLanguageItem
                toLanguageItem.languageName
            }
        binding.homeSelectLanguage.setToLanguage(toLanguage)
    }

    private fun setFromLanguage(fromLanguageItem: SearchLanguageItem.LanguageItem) {
        val fromLanguage =
            if (fromLanguageItem.languageName.isEmpty()) {
                getString(R.string.search)
            } else {
                viewModel.fromLanguageItem = fromLanguageItem
                fromLanguageItem.languageName
            }
        binding.homeSelectLanguage.setFromLanguage(fromLanguage)
    }

    private fun setUpHomeChooseLanguageView() {
        binding.homeSelectLanguage.apply {
            onClickFromLanguage = {
                showSearchBottomSheet { searchLanguageItem ->
                    val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                    setFromLanguage(languageItem.languageName)
                    viewModel.storeLanguageItem(true, languageItem)
                }
            }

            onClickToLanguage = {
                showSearchBottomSheet { searchLanguageItem ->
                    val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                    setToLanguage(languageItem.languageName)
                    viewModel.storeLanguageItem(false, languageItem)
                }
            }

            onClickSwitch = {
//                binding.ivSwitch.setOnClickListener {
//                    viewModel.swapLanguageItem(viewModel.fromLanguageItem, viewModel.toLanguageItem)
//                }
            }
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

            // Add swipe-to-delete functionality
            ItemTouchHelper(createTranslatedWordSwipeCallback()).attachToRecyclerView(this)
        }
    }

    private fun createTranslatedWordSwipeCallback() = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val translatedWord = translatedWordAdapter?.getItemAt(position)

            translatedWord?.let {
                // Remove from database
                viewModel.deleteTranslatedWord(it)

                // Optional: Show undo snackbar
                view?.let { view ->
                    Snackbar.make(
                        view,
                        "Item is deleted",
                        Snackbar.LENGTH_LONG
                    ).setAction("Undo") {
                        // Undo the deletion
                        viewModel.addTranslatedWord(translatedWord.originalWord, translatedWord.translatedWord)
                    }.show()
                }
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
            if (viewModel.fromLanguageItem.languageCode == "en") {
                viewModel.getWordDefinition(viewModel.originalText)
            }
            viewModel.addTranslatedWord(viewModel.originalText, viewModel.translatedText)
        }
        binding.translated.onClickSpeak = { text ->
            viewModel.speak(text, viewModel.toLanguageItem.languageCode)
        }
    }

    private fun setupOriginalView() {
        binding.original.showMic(true)
        binding.original.onClickMic = {
            // TODO Implements Mic
            val intent = speechIntent(viewModel.fromLanguageItem.languageCode)
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
            Log.d("AAAA", "onClick speak")
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

    private fun isShowMeaning(isShow: Boolean) {
        binding.rcvMeaning.showOrGone(isShow)
    }

    private fun isShowTranslatedWord(isShow: Boolean) {
        binding.rcvTranslatedWord.showOrGone(isShow)
    }

    private fun resetAll() {
        resetText()
        hidePhonetic()
        hideSpeak()
        isShowMeaning(false)
        isShowTranslatedWord(true)

        viewModel.originalText = ""
        viewModel.translatedText = ""
    }

    private fun setupOnClickView() {
        binding.ivImage.setOnClickListener {
            navigateToActivity(TranslateImageActivity::class.java)
        }

        binding.ivCamera.setOnClickListener {
            navigateToActivity(TranslateCameraXActivity::class.java)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
