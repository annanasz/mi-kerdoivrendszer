package com.bme.surveysystemsupportedbyai.ui.sentsurveydetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.ui.sentsurveydetails.components.ResponsesScreen
import com.bme.surveysystemsupportedbyai.ui.sentsurveydetails.components.SentSurveyDetailsScreenTopBar
import com.bme.surveysystemsupportedbyai.ui.surveyDetails.DetailsScreenUiState
import com.bme.surveysystemsupportedbyai.ui.surveyDetails.SurveyDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentSurveyDetailsScreen(
    navigateBack: () -> Unit,
    openDetailsScreen: (String, String) -> Unit,
    viewModel: SentSurveyDetailsViewModel = hiltViewModel()
) {
    val tabIndex = viewModel.selectedTab
    val tabs = listOf("Questions", "Responses")
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.fetchData()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SentSurveyDetailsScreenTopBar(navigateBack, uiState.survey.title) }) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = tabIndex, indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[tabIndex])
                )
            }) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index,
                        onClick = { viewModel.selectedTab = index },
                        text = { Text(title) },
                        enabled = uiState.survey.id.isNotEmpty()
                    )
                }
            }
            when (tabIndex) {
                0 -> SurveyTab(uiState) { option -> viewModel.onOptionSelected(option) }
                1 -> ResponsesTab(openDetailsScreen, uiState as SentSurveyDetailsScreenUiState)
            }
        }
    }
}

@Composable
fun SurveyTab(uiState: DetailsScreenUiState, onOptionSelected: (String) -> Unit) {
    SurveyDetailsContent(
        uiState = uiState, onOptionSelected = onOptionSelected, padding = PaddingValues(0.dp)
    )
}

@Composable
fun ResponsesTab(
    openDetailsScreen: (String, String) -> Unit, uiState: SentSurveyDetailsScreenUiState
) {
    ResponsesScreen(openDetailsScreen = openDetailsScreen, uiState = uiState)
}