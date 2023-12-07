package com.bme.surveysystemsupportedbyai.ui.sentsurveydetails.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import com.bme.surveysystemsupportedbyai.ui.sentsurveydetails.SentSurveyDetailsScreenUiState

@Composable
fun ResponsesScreen(
    openDetailsScreen: (String, String) -> Unit,
    uiState: SentSurveyDetailsScreenUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (uiState.responses.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Center
            ) {
                Text(text = "No responses yet")
            }
        } else {
            RecipientList(responses = uiState.responses, openDetailsScreen = openDetailsScreen)
        }
    }
}

