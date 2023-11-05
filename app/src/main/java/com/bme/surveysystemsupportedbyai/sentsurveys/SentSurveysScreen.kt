package com.bme.surveysystemsupportedbyai.sentsurveys

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
import com.bme.surveysystemsupportedbyai.sentsurveys.components.SentSurveyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentSurveysSurveyScreen(
    viewModel: SentSurveysViewModel = hiltViewModel(),
    openDetailsScreen: (String) -> Unit
) {
    val sentSurveys = viewModel.sentSurveys.collectAsStateWithLifecycle(initialValue = emptyList())
    Scaffold(
        topBar = {
            TopBar(
                title = Constants.SENT_SURVEYS_SCREEN,
                signOut = {
                    viewModel.signOut()
                }
            )
        }
    ){
        innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SentSurveyList(surveys = sentSurveys.value, openDetailsScreen)
        }
    }

}