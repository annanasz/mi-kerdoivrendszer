package com.bme.surveysystemsupportedbyai.ui.scansurveyscreen

import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.ui.scansurveyscreen.components.CameraScreen
import com.bme.surveysystemsupportedbyai.ui.scansurveyscreen.components.NoPermissionScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanSurveyScreen(
    navigateBack: () -> Unit,
    openDetailsScreen: (String) -> Unit,
) {
    val cameraPermissionState: PermissionState =
        rememberPermissionState(android.Manifest.permission.CAMERA)
    ScanSurveyContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest,
        navigateBack = navigateBack,
        openDetailsScreen = openDetailsScreen
    )
}

@Composable
private fun ScanSurveyContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    navigateBack: () -> Unit,
    openDetailsScreen: (String) -> Unit
) {
    if (hasPermission) {
        CameraScreen(navigateBack, openDetailsScreen)
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}
