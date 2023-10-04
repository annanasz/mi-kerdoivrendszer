package com.bme.surveysystemsupportedbyai.filledoutsurveys

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.filledoutsurveys.components.FilledOutSurveysContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledOutSurveyScreen(
    viewModel: FilledOutSurveysViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopBar(
                title = Constants.FILLED_OUT_SURVEYS_SCREEN,
                signOut = {
                    viewModel.signOut()
                }
            )
        },
        content = { padding ->
            FilledOutSurveysContent(
                padding = padding
            )
        }
    )

}