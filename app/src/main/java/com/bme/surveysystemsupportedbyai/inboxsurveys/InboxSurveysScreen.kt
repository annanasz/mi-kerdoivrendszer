package com.bme.surveysystemsupportedbyai.inboxsurveys

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.inboxsurveys.components.InboxSurveysContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxSurveyScreen(
    viewModel: InboxSurveysViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopBar(
                title = Constants.INBOX_SURVEYS_SCREEN,
                signOut = {
                    viewModel.signOut()
                }
            )
        },
        content = { padding ->
            InboxSurveysContent(
                padding = padding
            )
        }
    )

}