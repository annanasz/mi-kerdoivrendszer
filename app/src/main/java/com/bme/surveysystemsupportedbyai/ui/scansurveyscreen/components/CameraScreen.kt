package com.bme.surveysystemsupportedbyai.ui.scansurveyscreen.components

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.ui.filloutwithspeech.LoadingAnimation
import com.bme.surveysystemsupportedbyai.ui.scansurveyscreen.ScanSurveyViewModel

@Composable
fun CameraScreen(
    navigateBack: () -> Unit,
    openDetailsScreen: (String) -> Unit
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
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(text = { Text("Scan survey") }, onClick = {
                viewModel.capturePhoto(context, cameraController)
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
        if (uiState.isDialogOpen) {
            AlertDialog(onDismissRequest = {
            }, title = { Text("Captured Photo") },
                text = {
                    if(uiState.errorMessage.isNotEmpty())
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(uiState.errorMessage)
                        }
                    }
                    else
                    {
                        if(!uiState.loading){
                            Column {
                                uiState.capturedPhoto?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = uiState.uppercase,
                                        onCheckedChange = { isChecked ->
                                            viewModel.onUpperCaseClicked(isChecked)
                                        }
                                    )
                                    Text("Capital letters")
                                }
                            }
                        }
                        else{
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingAnimation()
                                Text("Processing image...")
                            }
                        }
                    }
            }, confirmButton = {
                Button(onClick = {
                    viewModel.processImage(openDetailsScreen)
                }, enabled=uiState.errorMessage.isEmpty() && !uiState.loading) {
                    Text("Accept")
                }
            }, dismissButton = {
                Button(onClick = {
                    viewModel.onDismissClick()
                }) {
                    Text("Retake")
                }
            })
        }
    }
}

