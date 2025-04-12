package `in`.rchandel.qcypher.ui.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Size
import android.widget.FrameLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import `in`.rchandel.qcypher.R
import `in`.rchandel.qcypher.tools.QrAnalyser
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun ScannerScreen(
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    onScanSuccess: (String) -> Unit,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var cameraState by remember { mutableStateOf<Camera?>(null) }
    var zoomLevel by remember { mutableStateOf(0f) }
    var lensFacingState by remember { mutableIntStateOf(lensFacing) }

    val hasCameraPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission.value = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission.value) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
//            viewModel.bindCamera(context, lifecycleOwner, cameraProviderFuture, lensFacingState) {
//                cameraState = it
//            }
        }
    }

    // Listen to scan result
    LaunchedEffect(Unit) {
        viewModel.scanResult.collect { result ->
            onScanSuccess(Uri.encode(result))
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                val cameraProvider = cameraProviderFuture.get()
                viewModel.unbindCamera(cameraProvider)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    // UI Layout
    if (hasCameraPermission.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            // The ViewModel bindCamera() will set the surface provider on this instance
                        }.also { previewView ->
                            // IMPORTANT: bind the camera using this actual PreviewView
                            viewModel.bindCameraWithPreviewView(
                                ctx,
                                lifecycleOwner,
                                cameraProviderFuture,
                                lensFacingState,
                                previewView
                            ) { camera ->
                                cameraState = camera
                            }
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(color = Color.Black.copy(alpha = 0.7f))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .fillMaxHeight()
                            .background(color = Color.Black.copy(alpha = 0.7f))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .fillMaxHeight()
                            .background(color = Color.Black.copy(alpha = 0.7f))
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(color = Color.Black.copy(alpha = 0.7f))
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .align(alignment = Alignment.TopCenter)
            ) {
                Card(
                    modifier = Modifier
                        .background(color = Color.Black)
                        .padding(8.dp)
                        .fillMaxWidth(0.7f),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(painter = painterResource(id = R.drawable.baseline_history_24),
                            contentDescription = "", modifier = Modifier.clickable {

                            })
                        SwitchCameraButton {
                            if (lensFacingState == CameraSelector.LENS_FACING_FRONT) {
                                lensFacingState = CameraSelector.LENS_FACING_BACK
                            } else if (lensFacingState == CameraSelector.LENS_FACING_BACK) {
                                lensFacingState =
                                    CameraSelector.LENS_FACING_FRONT
                            }
                        }
                        TorchView(cameraState = cameraState)
                    }
                }

            }

            Slider(
                value = zoomLevel,
                onValueChange = {
                    zoomLevel = it
                    cameraState?.cameraControl?.setLinearZoom(it / 10)
                },
                valueRange = 0f..(cameraState?.cameraInfo?.zoomState?.value?.maxZoomRatio
                    ?: 10f), // Adjust range based on device capability
                modifier = Modifier
                    .width(200.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission is required to scan QR codes.")
        }
    }

}

@Composable
fun letsSee(
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    onScanSuccess: (String) -> Unit
) {
    var code by remember {
        mutableStateOf("")
    }

    var camera: Camera? = null

    var cameraState by remember {
        mutableStateOf(camera)
    }

    var zoomLevel by remember { mutableStateOf(0f) } // Zoom state

    var lensFacingState by remember {
        mutableIntStateOf(lensFacing)
    }
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        })

    LaunchedEffect(key1 = true) {
        launcher.launch(android.Manifest.permission.CAMERA)
    }

    key(lensFacingState) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (hasCameraPermission) {

                    AndroidView(modifier = Modifier.weight(1f), factory = { context ->
                        val previewView = PreviewView(context)
                        val preview = androidx.camera.core.Preview.Builder().build()
                        cameraProviderFuture.get().unbindAll()
                        val selectorState = CameraSelector.Builder()
                            .requireLensFacing(lensFacingState)
                            .build()

                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(Size(previewView.width, previewView.height))
                            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QrAnalyser { result ->
                                code = result
                                onScanSuccess(Uri.encode(result))
                                cameraProviderFuture.get().unbindAll()
                            }
                        )

                        try {
                            cameraState = cameraProviderFuture.get().bindToLifecycle(
                                lifeCycleOwner,
                                selectorState,
                                preview,
                                imageAnalysis
                            )
                            previewView.controller = cameraController
                            cameraController.bindToLifecycle(lifeCycleOwner)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        previewView
                    })
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(color = Color.Black.copy(alpha = 0.7f))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .fillMaxHeight()
                            .background(color = Color.Black.copy(alpha = 0.7f))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .fillMaxHeight()
                            .background(color = Color.Black.copy(alpha = 0.7f))
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(color = Color.Black.copy(alpha = 0.7f))
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .align(alignment = Alignment.TopCenter)
            ) {
                Card(
                    modifier = Modifier
                        .background(color = Color.Black)
                        .padding(8.dp)
                        .fillMaxWidth(0.7f),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(painter = painterResource(id = R.drawable.baseline_history_24),
                            contentDescription = "", modifier = Modifier.clickable {

                            })
                        SwitchCameraButton {
                            if (lensFacingState == CameraSelector.LENS_FACING_FRONT) {
                                lensFacingState = CameraSelector.LENS_FACING_BACK
                            } else if (lensFacingState == CameraSelector.LENS_FACING_BACK) {
                                lensFacingState =
                                    CameraSelector.LENS_FACING_FRONT
                            }
                        }
                        TorchView(cameraState = cameraState)
                    }
                }

            }

            Slider(
                value = zoomLevel,
                onValueChange = {
                    zoomLevel = it
                    cameraState?.cameraControl?.setLinearZoom(it / 10)
                },
                valueRange = 0f..(cameraState?.cameraInfo?.zoomState?.value?.maxZoomRatio
                    ?: 10f), // Adjust range based on device capability
                modifier = Modifier
                    .width(200.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
            )

        }
    }
}

@Composable
fun SwitchCameraButton(onButtonClick: () -> Unit) {

    Image(
        painter = painterResource(id = R.drawable.baseline_flip_camera_ios_24),
        contentDescription = "",
        modifier = Modifier
            .clickable {
                onButtonClick()

            }
    )
}

@Composable
fun TorchView(cameraState: Camera?) {

    var torchOnState by remember {
        mutableStateOf(false)
    }

    Image(
        painter = painterResource(id = if (torchOnState) R.drawable.baseline_flashlight_on_24 else R.drawable.baseline_flashlight_off_24),
        contentDescription = "",
        modifier = Modifier
            .clickable {
                if (cameraState?.cameraInfo?.hasFlashUnit() == true) {
                    cameraState.cameraInfo.torchState.value.let { it ->
                        if (it == TorchState.ON) {
                            torchOnState = false
                            cameraState.cameraControl.enableTorch(false)
                        } else {
                            torchOnState = true
                            cameraState.cameraControl.enableTorch(true)
                        }
                    }
                }
            }
    )
}