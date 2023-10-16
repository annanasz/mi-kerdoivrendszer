package com.bme.surveysystemsupportedbyai.scansurveyscreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.scansurveyscreen.components.CameraScreen
import com.bme.surveysystemsupportedbyai.scansurveyscreen.components.NoPermissionScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanSurveyScreen ( navigateBack: () -> Unit,
                       viewModel: ScanSurveyViewModel = hiltViewModel(),
                       openDetailsScreen: (String) -> Unit,
){
    val cameraPermissionState:PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    ScanSurveyContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest,
        navigateBack = navigateBack,
        openDetailsScreen = openDetailsScreen,
        viewModel = viewModel
    )
}

@Composable
private fun ScanSurveyContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    navigateBack: () -> Unit,
    openDetailsScreen: (String) -> Unit,
    viewModel: ScanSurveyViewModel
){
    if(hasPermission){
        CameraScreen(navigateBack, openDetailsScreen)
    }
    else{
        NoPermissionScreen(onRequestPermission)
    }
}
