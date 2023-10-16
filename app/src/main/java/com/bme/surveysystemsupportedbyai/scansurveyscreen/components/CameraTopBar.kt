package com.bme.surveysystemsupportedbyai.scansurveyscreen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.core.Constants.SCAN_MESSAGE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text(text = SCAN_MESSAGE) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}