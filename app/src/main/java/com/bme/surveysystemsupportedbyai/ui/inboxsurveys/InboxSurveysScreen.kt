package com.bme.surveysystemsupportedbyai.ui.inboxsurveys

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.ui.filloutwithspeech.LoadingAnimation
import com.bme.surveysystemsupportedbyai.ui.inboxsurveys.components.InboxSurveyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxSurveyScreen(
    viewModel: InboxSurveysViewModel = hiltViewModel(),
    openFillOutScreen: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.fetchData()
    }

    Scaffold(topBar = {
        TopBar(title = Constants.INBOX_SURVEYS_SCREEN, signOut = {
            viewModel.signOut()
        })
    }) { innerPadding ->
        if (viewModel.loading.value) Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            LoadingAnimation()
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                InboxSurveyList(surveys = uiState.receivedSurveys,
                    onFillOutClick = { receivedSurvey ->
                        viewModel.onFillOutClick(
                            receivedSurvey, openFillOutScreen
                        )
                    },
                    onFillOutWithSpeechClick = { receivedSurvey ->
                        viewModel.onFillOutWithSpeechClick(receivedSurvey, openFillOutScreen)
                    })
            }
        }
    }
}