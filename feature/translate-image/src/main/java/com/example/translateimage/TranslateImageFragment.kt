package com.example.translateimage

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.scale
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mlkit.overlay.TextGraphic
import com.example.mlkit.utils.BitmapUtils
import com.example.model.SearchLanguageItem
import com.example.translateimage.databinding.FragmentTranslateImageBinding
import com.example.ui.R
import com.example.ui.base.BaseFragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TranslateImageFragment :
    BaseFragment<FragmentTranslateImageBinding, TranslateImageViewmodel>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentTranslateImageBinding {
        return FragmentTranslateImageBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: TranslateImageViewmodel by viewModels()

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            uri?.let {
                Log.d("PhotoPicker", "Selected URI: $uri")
                viewModel.uri = it
                detectTextInImage(it)
            } ?: run {
                Log.d("PhotoPicker", "No media selected")
            }
        }

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
                    binding.graphicOverlay.clear()
                    binding.graphicOverlay.add(
                        TextGraphic(
                            binding.graphicOverlay,
                            listTextDrawing,
                        ),
                    )
                    binding.graphicOverlay.invalidate()
                }
            }

            launch {
                viewModel.pairLanguageFlow.collect { (fromLanguageItem, toLanguageItem) ->
                    binding.imageChooseLanguageView.setFromLanguage(
                        fromLanguageItem.languageName.ifEmpty {
                            getString(
                                R.string.search,
                            )
                        },
                    )

                    binding.imageChooseLanguageView.setToLanguage(
                        toLanguageItem.languageName.ifEmpty {
                            getString(R.string.search)
                        },
                    )

                    val pairLanguageItem = viewModel.pairLanguageFlow.value
                    if (pairLanguageItem.first.languageCode.isNotEmpty() &&
                        pairLanguageItem.second.languageCode.isNotEmpty()
                    ) {
                        viewModel.uri?.let { uri ->
                            detectTextInImage(uri)
                        }
                    }
                }
            }

            launch {
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.uri?.let {
            detectTextInImage(it)
        }
    }

    private fun detectTextInImage(imageUri: Uri) {
        val imageBitmap =
            BitmapUtils.getBitmapFromContentUri(requireActivity().contentResolver, imageUri)
                ?: return

        val targetedSize: Pair<Int, Int> = Pair(binding.root.width, binding.root.height)

        // Determine how much to scale down the image
        val scaleFactor =
            Math.max(
                imageBitmap.width.toFloat() / targetedSize.first.toFloat(),
                imageBitmap.height.toFloat() / targetedSize.second.toFloat(),
            )

        val resizedBitmap: Bitmap =
            imageBitmap.scale(
                (imageBitmap.width / scaleFactor).toInt(),
                (imageBitmap.height / scaleFactor).toInt(),
            )

        binding.ivSelectedImage.setImageBitmap(resizedBitmap)
        viewModel.processImage(
            InputImage.fromBitmap(resizedBitmap, 0),
            TextRecognizerOptions.DEFAULT_OPTIONS,
        )
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
        }
    }

    companion object {
        fun newInstance() =
            TranslateImageFragment().apply {
            }
    }
}
