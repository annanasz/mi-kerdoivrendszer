package com.bme.surveysystemsupportedbyai.sentsurveys

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.sentsurveys.components.SentSurveysContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentSurveysSurveyScreen(
    viewModel: SentSurveysViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopBar(
                title = Constants.SENT_SURVEYS_SCREEN,
                signOut = {
                    viewModel.signOut()
                }
            )
        },
        content = { padding ->
            SentSurveysContent(
                padding = padding
            )
        }
    )

}