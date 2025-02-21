package com.example.translator.presentation.translateImage

import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.translator.databinding.FragmentTranslateImageBinding
import com.example.translator.presentation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TranslateImageFragment :
    BaseFragment<FragmentTranslateImageBinding, TranslateImageViewmodel>() {

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        uri?.let {
            Log.d("PhotoPicker", "Selected URI: $uri")
            binding.ivSelectedImage.setImageURI(uri)
            viewModel.textRecognition(uri, binding.ivSelectedImage.imageMatrix)
            Log.d(
                "AAAA",
                "imageView height = ${binding.ivSelectedImage.height}, width = ${binding.ivSelectedImage.width}"
            )
        } ?: run {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentTranslateImageBinding {
        return FragmentTranslateImageBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: TranslateImageViewmodel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        lifecycleScope.launch {
            launch {
                viewModel.listTextDrawing.collect { listTextDrawing ->
                    binding.overlayView.updateTextDrawing(listTextDrawing)
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