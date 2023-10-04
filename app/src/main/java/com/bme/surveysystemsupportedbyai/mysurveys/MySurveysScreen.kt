package com.bme.surveysystemsupportedbyai.mysurveys

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.mysurveys.components.SurveyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySurveysSurveyScreen(
    openDetailsScreen: (String) -> Unit,
    viewModel: MySurveysViewModel = hiltViewModel()
) {
    val mySurveys = viewModel.surveys.collectAsState(emptyList())
    Scaffold(
        topBar = {
            TopBar(
                title = Constants.MY_SURVEYS_SCREEN,
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
            SurveyList(
                //surveys = mySurveys,
                surveys = viewModel.surveys_dummy,
                onEditClick = { survey -> viewModel.onEditClick(survey,openScreen = openDetailsScreen) },
                onDeleteClick = { survey -> viewModel.onDeleteClick(survey) },
                onSendClick ={ survey -> viewModel.onSendClick(survey) } ,
                onItemClick = { survey -> viewModel.onItemClick(survey =  survey, openScreen = openDetailsScreen) }
            )
        }
    }
}