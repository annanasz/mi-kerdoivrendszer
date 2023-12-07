package com.bme.surveysystemsupportedbyai.ui.filledoutsurveys

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.bme.surveysystemsupportedbyai.ui.filledoutsurveys.components.FilledOutList
import com.bme.surveysystemsupportedbyai.ui.filloutwithspeech.LoadingAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledOutSurveyScreen(
    viewModel: FilledOutSurveysViewModel = hiltViewModel(),
    openDetailsScreen: (String) -> Unit,
    paddingValues: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.fetchData()
    }

    Scaffold(topBar = {
        TopBar(title = Constants.FILLED_OUT_SURVEYS_SCREEN, signOut = {
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
                    .padding(
                        PaddingValues(
                            bottom = maxOf(
                                innerPadding.calculateBottomPadding(),
                                paddingValues.calculateBottomPadding()
                            ), top = maxOf(
                                innerPadding.calculateTopPadding(),
                                paddingValues.calculateTopPadding()
                            )
                        )
                    )
                    .fillMaxSize()
            ) {
                FilledOutList(responses = uiState.filledOutSurveys, onItemClick = { response ->
                    viewModel.onItemClick(
                        response, openDetailsScreen
                    )
                })
            }
        }
    }
}

