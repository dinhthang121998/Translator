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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.common.R
import com.example.mlkit.TextRecognition
import com.example.mlkit.overlay.TextGraphic
import com.example.mlkit.utils.BitmapUtils
import com.example.model.SearchLanguageItem
import com.example.translateimage.databinding.FragmentTranslateImageBinding
import com.example.ui.base.BaseFragment
import com.google.mlkit.vision.common.InputImage
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

//                    onFailure = {
//                        binding.graphicOverlay.clear()
//                        binding.graphicOverlay.postInvalidate()
//                    }
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
                            detectTextInImage(uri)
                        }
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
        val imageBitmap = BitmapUtils.getBitmapFromContentUri(requireActivity().contentResolver, imageUri) ?: return

        val targetedSize: Pair<Int, Int> = Pair(binding.root.width, binding.root.height)

        // Determine how much to scale down the image
        val scaleFactor =
            Math.max(
                imageBitmap.width.toFloat() / targetedSize.first.toFloat(),
                imageBitmap.height.toFloat() / targetedSize.second.toFloat(),
            )

        val resizedBitmap: Bitmap =
            Bitmap.createScaledBitmap(
                imageBitmap,
                (imageBitmap.width / scaleFactor).toInt(),
                (imageBitmap.height / scaleFactor).toInt(),
                true,
            )

        binding.ivSelectedImage.setImageBitmap(resizedBitmap)
        viewModel.processImage(InputImage.fromBitmap(resizedBitmap, 0), TextRecognition.LATIN_RECOGNITION)
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
