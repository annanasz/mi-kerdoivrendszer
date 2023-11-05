package com.bme.surveysystemsupportedbyai.sentsurveydetails

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.sentsurveydetails.components.ResponsesScreen
import com.bme.surveysystemsupportedbyai.sentsurveydetails.components.SentSurveyDetailsScreenTopBar
import com.bme.surveysystemsupportedbyai.surveyDetails.DetailsScreenViewModel
import com.bme.surveysystemsupportedbyai.surveyDetails.SurveyDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentSurveyDetailsScreen(
    navigateBack: () -> Unit,
    openDetailsScreen: (String, String) -> Unit,
    viewModel: SentSurveyDetailsViewModel = hiltViewModel()
) {
    val tabIndex = viewModel.selectedTab
    val tabs = listOf("Questions", "Responses")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SentSurveyDetailsScreenTopBar(navigateBack) }) { paddingValues: PaddingValues ->
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
                        enabled = viewModel.survey.value.id.isNotEmpty())
                }
            }
            when (tabIndex) {
                0 -> Tab1Screen(viewModel)
                1 -> Tab2Screen(openDetailsScreen, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Tab1Screen(viewModel: DetailsScreenViewModel) {
    SurveyDetailsContent(viewModel = viewModel, padding = PaddingValues(0.dp))
}

@Composable
fun Tab2Screen(openDetailsScreen: (String, String) -> Unit,viewModel: SentSurveyDetailsViewModel) {
    ResponsesScreen(openDetailsScreen = openDetailsScreen,viewModel = viewModel)
}