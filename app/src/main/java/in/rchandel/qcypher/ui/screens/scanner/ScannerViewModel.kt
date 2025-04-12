package `in`.rchandel.qcypher.ui.screens.scanner

import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.rchandel.qcypher.tools.QrAnalyser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor() : ViewModel() {

    private val _scanResult = MutableSharedFlow<String>(replay = 1)
    val scanResult: SharedFlow<String> = _scanResult

    fun bindCameraWithPreviewView(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
        lensFacing: Int,
        previewView: PreviewView,
        onCameraReady: (Camera) -> Unit
    ) {
        val cameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()

        val preview = Preview.Builder().build()
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), QrAnalyser { result ->
            viewModelScope.launch(Dispatchers.IO) {
                _scanResult.emit(result)
            }
        })

        val selector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        try {
            val camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                selector,
                preview,
                imageAnalysis
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
            onCameraReady(camera)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun unbindCamera(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()
    }
}