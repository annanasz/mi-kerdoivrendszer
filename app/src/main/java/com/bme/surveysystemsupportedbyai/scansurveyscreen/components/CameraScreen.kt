package com.bme.surveysystemsupportedbyai.scansurveyscreen.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.scansurveyscreen.ScanSurveyViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.Executor


@Composable
fun CameraScreen(
    navigateBack: () -> Unit,
    openDetailsScreen: (String) -> Unit,
    viewModel: ScanSurveyViewModel = hiltViewModel()
) {
    CameraContent(navigateBack, openDetailsScreen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CameraContent(
    navigateBack: () -> Unit,
    openDetailsScreen: (String) -> Unit,
    viewModel: ScanSurveyViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var capturedPhoto: Bitmap? by remember { mutableStateOf(null) }

    Scaffold(modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(text = { Text("Scan survey") }, onClick = {
                capturePhoto(context, cameraController) { photo ->
                    capturedPhoto = photo
                    isDialogOpen = true
                }
            }, icon = {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera capture icon"
                )
            })
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = { CameraTopBar(navigateBack) }) { paddingValues: PaddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                })
        }
        if (isDialogOpen) {
            AlertDialog(onDismissRequest = {
                isDialogOpen = false
            }, title = { Text("Captured Photo") }, text = {
                capturedPhoto?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
            }, confirmButton = {
                Button(onClick = {
                    capturedPhoto?.let {
                        val surveyId = processImage(it) { visionText,_ ->
                            viewModel.processTextResult(visionText) { textResult ->
                                if(textResult!=null)
                                    viewModel.openEditScreen(textResult,openDetailsScreen)
                            }
                        }
                        surveyId?.let { it1 -> viewModel.openEditScreen(it1, openDetailsScreen) }
                    }
                    isDialogOpen = false
                }) {
                    Text("Accept")
                }
            }, dismissButton = {
                Button(onClick = {
                    isDialogOpen = false
                    capturedPhoto = null
                }) {
                    Text("Retake")
                }
            })
        }
    }
}

private fun capturePhoto(
    context: Context, cameraController: LifecycleCameraController, onPhotoCaptured: (Bitmap) -> Unit
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val correctedBitmap =
                rotateBitmapIfNeeded(image.toBitmap(), image.imageInfo.rotationDegrees)
            image.close()
            onPhotoCaptured(correctedBitmap)
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}

private fun rotateBitmapIfNeeded(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
    return if (rotationDegrees == 0) {
        bitmap
    } else {
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}

private fun processImage(image: Bitmap, processText: (Text, (String?) -> Unit) -> Unit):String?{
    val inputImage = InputImage.fromBitmap(image, 0)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    var surveyId: String? = "hihi"
    val result = recognizer.process(inputImage)
        .addOnSuccessListener { visionText ->
            processText(visionText) { textResult ->
                surveyId = textResult
            }
        }
    return surveyId
}
