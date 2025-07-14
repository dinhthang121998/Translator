package com.example.translatecamerax

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mlkit.overlay.TextGraphic
import com.example.translatecamerax.databinding.FragmentTranslateCameraXBinding
import com.example.ui.base.BaseFragment
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class TranslateCameraXFragment :
    BaseFragment<FragmentTranslateCameraXBinding, TranslateCameraXViewmodel>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentTranslateCameraXBinding {
        return FragmentTranslateCameraXBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: TranslateCameraXViewmodel by viewModels()
    private lateinit var cameraExecutor: ExecutorService

    // for image capture
    private var imageCapture: ImageCapture? = null

    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var cameraSelector: CameraSelector =
        CameraSelector.Builder().requireLensFacing(lensFacing).build()
    private var imageProxy: ImageProxy? = null

    @OptIn(ExperimentalGetImage::class)
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(requireContext().applicationContext)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindAllCameraUseCases()
        }, ContextCompat.getMainExecutor(this.requireContext()))

        checkCameraPermission()

        lifecycleScope.launch {
            launch {
                viewModel.textStateFlow.collect { listTextDrawing ->

                    binding.graphicOverlay.clear()
// //                     Why we need CameraImageGraphic ?? // darker background??
//                    imageProxy?.let { imageProxy ->
//                        BitmapUtils.getBitmap(imageProxy)?.let { bitmap ->
//                            binding.graphicOverlay.add(
//                                CameraImageGraphic(
//                                    binding.graphicOverlay,
//                                    bitmap,
//                                ),
//                            )
//                        }
//                    }
                    binding.graphicOverlay.add(
                        TextGraphic(
                            binding.graphicOverlay,
                            listTextDrawing,
                        ),
                    )
                    binding.graphicOverlay.invalidate()
                }
            }
        }
    }

    private fun bindAllCameraUseCases() {
        if (cameraProvider != null) {
            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
            cameraProvider?.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun bindAnalysisUseCase() {
        if (cameraProvider == null) return
        if (analysisUseCase != null) {
            cameraProvider?.unbind(analysisUseCase)
        }

        val builder = ImageAnalysis.Builder()
        analysisUseCase = builder.build()

        needUpdateGraphicOverlayImageSourceInfo = true

        analysisUseCase?.setAnalyzer(
            // imageProcessor.processImageProxy will use another thread to run the detection underneath,
            // thus we can just runs the analyzer itself on main thread.
            ContextCompat.getMainExecutor(this.requireContext()),
            { imageProxy: ImageProxy ->
                // a proxy image refers to an intermediate image representation that
                // allows efficient processing before saving or displaying the final image
                this.imageProxy = imageProxy
                if (needUpdateGraphicOverlayImageSourceInfo) {
                    val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    if (rotationDegrees == 0 || rotationDegrees == 180) {
                        binding.graphicOverlay.setImageSourceInfo(
                            imageProxy.width,
                            imageProxy.height,
                            isImageFlipped,
                        )
                    } else {
                        binding.graphicOverlay.setImageSourceInfo(
                            imageProxy.height,
                            imageProxy.width,
                            isImageFlipped,
                        )
                    }
                    needUpdateGraphicOverlayImageSourceInfo = false
                }
                try {
                    viewModel.processImageProxy(imageProxy, TextRecognizerOptions.DEFAULT_OPTIONS)
                } catch (e: MlKitException) {
                    Toast.makeText(this.requireContext(), e.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            },
        )
        cameraProvider?.bindToLifecycle(this, cameraSelector, analysisUseCase)
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider?.unbind(previewUseCase)
        }

        val builder = Preview.Builder()
        previewUseCase = builder.build()
        previewUseCase?.surfaceProvider = binding.previewView.getSurfaceProvider()
        camera = cameraProvider?.bindToLifecycle(this, cameraSelector, previewUseCase)
    }

    private fun checkCameraPermission() {
        when {
            // If permission is already granted
            ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(
                    this.requireContext(),
                    "Camera permission granted",
                    Toast.LENGTH_SHORT,
                ).show()
            }

            // If permission is denied before, request again
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(
                    this.requireContext(),
                    "Camera permission is needed to take photos",
                    Toast.LENGTH_LONG,
                ).show()
                requestCameraPermission()
            }

            // Request permission for the first time
            else -> {
                requestCameraPermission()
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this.requireContext(), "Camera permission granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this.requireContext(), "Camera permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        fun newInstance() =
            TranslateCameraXFragment().apply {
            }
    }
}
