package com.bme.surveysystemsupportedbyai.mysurveys

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.mysurveys.components.DeleteAlertDialog
import com.bme.surveysystemsupportedbyai.mysurveys.components.SendSurveyDialog
import com.bme.surveysystemsupportedbyai.mysurveys.components.SurveyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySurveysSurveyScreen(
    openDetailsScreen: (String) -> Unit,
    openScanSurveyScreen: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: MySurveysViewModel = hiltViewModel()
) {
    val mySurveys = viewModel.surveys.collectAsStateWithLifecycle(emptyList())
    val emailList by remember { mutableStateOf("") }
    var deleteSurvey: Survey? by remember { mutableStateOf(null) }
    var sendSurvey: Survey? by remember { mutableStateOf(null) }
    var showSendSurveyDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(
                title = Constants.MY_SURVEYS_SCREEN,
                signOut = {
                    viewModel.signOut()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openScanSurveyScreen()
                },
                modifier = Modifier
                    .padding(16.dp, 16.dp, 16.dp, paddingValues.calculateBottomPadding() + 16.dp)
                    .size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(4.dp, innerPadding.calculateTopPadding(), 4.dp, paddingValues.calculateBottomPadding() + 16.dp)
                .fillMaxSize()
        ) {
            SurveyList(
                surveys = mySurveys.value,
                onEditClick = { survey ->
                    viewModel.onEditClick(
                        survey,
                        openScreen = openDetailsScreen
                    )
                },
                onDeleteClick = { survey ->
                    deleteSurvey = survey
                },
                onSendClick = { survey ->
                    showSendSurveyDialog = true
                    sendSurvey = survey
                },
                onItemClick = { survey ->
                    viewModel.onItemClick(
                        survey = survey,
                        openScreen = openDetailsScreen
                    )
                }
            )

        }
    }
    if (deleteSurvey != null) {
        DeleteAlertDialog(
            onDeleteClick = { survey ->
                viewModel.onDeleteClick(survey)
            },
            surveyToDelete = deleteSurvey,
            onDismiss = {
                deleteSurvey = null
            }
        )
    }
    if (showSendSurveyDialog) {
        SendSurveyDialog(
            onSendClick = { survey, emails ->
                viewModel.onSendSurveyClick(survey, emails)
                showSendSurveyDialog = false
            },
            onCancelClick = {
                showSendSurveyDialog = false
            },
            emailList = emailList,
            selectedSurvey = sendSurvey
        )
    }

}
