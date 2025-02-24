package com.example.translateimage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.common.R
import com.example.translateimage.databinding.FragmentTranslateImageBinding
import com.example.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TranslateImageFragment :
    BaseFragment<FragmentTranslateImageBinding, TranslateImageViewmodel>() {
    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            uri?.let {
                Log.d("PhotoPicker", "Selected URI: $uri")
                binding.ivSelectedImage.setImageURI(uri)
                viewModel.textRecognition(
                    uri,
                    binding.ivSelectedImage.imageMatrix,
                    viewModel.fromLanguageItem.languageCode,
                    viewModel.toLanguageItem.languageCode,
                )
            } ?: run {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentTranslateImageBinding {
        return FragmentTranslateImageBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: TranslateImageViewmodel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        viewModel.observePairLanguageItemChange()
        setUpOnClick()
        setUpMediaChooseLanguageView()

        lifecycleScope.launch {
            launch {
                viewModel.listTextDrawing.collect { listTextDrawing ->
                    binding.overlayView.updateTextDrawing(listTextDrawing)
                }
            }

            launch {
                viewModel.pairLanguageFlow.collect { (fromLanguageItem, toLanguageItem) ->
                    if (fromLanguageItem.languageName.isEmpty()) {
                        binding.imageChooseLanguageView.setFromLanguage(getString(R.string.search))
                    } else {
                        viewModel.fromLanguageItem = fromLanguageItem
                        binding.imageChooseLanguageView.setFromLanguage(fromLanguageItem.languageName)
                    }

                    if (fromLanguageItem.languageName.isEmpty()) {
                        binding.imageChooseLanguageView.setToLanguage(getString(R.string.search))
                    } else {
                        viewModel.toLanguageItem = toLanguageItem
                        binding.imageChooseLanguageView.setToLanguage(toLanguageItem.languageName)
                    }

                    if (viewModel.fromLanguageItem.languageCode.isNotEmpty() &&
                        viewModel.toLanguageItem.languageCode.isNotEmpty()
                    ) {
                        viewModel.uri?.let { uri ->
                            viewModel.textRecognition(
                                uri,
                                binding.ivSelectedImage.imageMatrix,
                                viewModel.fromLanguageItem.languageCode,
                                viewModel.toLanguageItem.languageCode,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setUpOnClick() {
        binding.ivBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setUpMediaChooseLanguageView() {
        binding.imageChooseLanguageView.apply {
            onClickFromLanguage = {
                showSearchBottomSheet { searchLanguageItem ->
                    val languageItem = searchLanguageItem as com.example.model.SearchLanguageItem.LanguageItem
                    setFromLanguage(languageItem.languageName)
                    viewModel.storeLanguageItem(true, languageItem)
                }
            }

            onClickToLanguage = {
                showSearchBottomSheet { searchLanguageItem ->
                    val languageItem = searchLanguageItem as com.example.model.SearchLanguageItem.LanguageItem
                    setToLanguage(languageItem.languageName)
                    viewModel.storeLanguageItem(false, languageItem)
                }
            }
        }
    }

    companion object {
        fun newInstance() =
            TranslateImageFragment().apply {
            }
    }
}
