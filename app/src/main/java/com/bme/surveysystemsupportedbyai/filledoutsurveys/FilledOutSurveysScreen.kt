package com.bme.surveysystemsupportedbyai.filledoutsurveys

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.filledoutsurveys.components.FilledOutList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledOutSurveyScreen(
    viewModel: FilledOutSurveysViewModel = hiltViewModel(),
    openDetailsScreen: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.fetchData()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = Constants.FILLED_OUT_SURVEYS_SCREEN,
                signOut = {
                    viewModel.signOut()
                }
            )
        }
    ){
        innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            FilledOutList(responses = uiState.filledOutSurveys, onItemClick = {response-> viewModel.onItemClick(response, openDetailsScreen) })
        }
    }
}

