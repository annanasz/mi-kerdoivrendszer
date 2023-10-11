package com.bme.surveysystemsupportedbyai.inboxsurveys

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.inboxsurveys.components.InboxSurveyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxSurveyScreen(
    viewModel: InboxSurveysViewModel = hiltViewModel(),
    openFillOutScreen: (String) -> Unit,
) {
    val receivedSurveys = viewModel.receivedSurveys.collectAsStateWithLifecycle(emptyList())
    Scaffold(
        topBar = {
            TopBar(
                title = Constants.INBOX_SURVEYS_SCREEN,
                signOut = {
                    viewModel.signOut()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            InboxSurveyList(
                surveys = receivedSurveys.value,
                onFillOutClick = { receivedSurvey ->
                    viewModel.onFillOutClick(
                        receivedSurvey,
                        openFillOutScreen
                    )
                })
        }
    }

}