package com.bme.surveysystemsupportedbyai.sentsurveydetails.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.core.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentSurveyDetailsScreenTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text(text = Constants.SENT_SURVEY) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}