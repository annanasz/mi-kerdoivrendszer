package com.bme.surveysystemsupportedbyai.ui.sentsurveydetails.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.core.Constants.titleFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentSurveyDetailsScreenTopBar(
    navigateBack: () -> Unit,
    title: String
) {
    TopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        title = { Text(text = title, fontFamily = titleFontFamily) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}