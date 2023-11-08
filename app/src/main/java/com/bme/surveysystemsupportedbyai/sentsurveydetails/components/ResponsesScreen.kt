package com.bme.surveysystemsupportedbyai.sentsurveydetails.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.sentsurveydetails.SentSurveyDetailsScreenUiState
import com.bme.surveysystemsupportedbyai.sentsurveydetails.SentSurveyDetailsViewModel
import com.bme.surveysystemsupportedbyai.surveyDetails.DetailsScreenUiState

@Composable
fun ResponsesScreen(
    openDetailsScreen: (String, String) -> Unit,
    uiState:SentSurveyDetailsScreenUiState
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

