package com.bme.surveysystemsupportedbyai.ui.mysurveys

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.ui.filloutwithspeech.LoadingAnimation
import com.bme.surveysystemsupportedbyai.ui.mysurveys.components.DeleteAlertDialog
import com.bme.surveysystemsupportedbyai.ui.mysurveys.components.SendSurveyDialog
import com.bme.surveysystemsupportedbyai.ui.mysurveys.components.SurveyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySurveysSurveyScreen(
    openDetailsScreen: (String) -> Unit,
    openScanSurveyScreen: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: MySurveysViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.fetchData()

    }
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
        if (viewModel.loading.value)
            Box(
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
                        0.dp,
                        innerPadding.calculateTopPadding(),
                        0.dp,
                        paddingValues.calculateBottomPadding() + 16.dp
                    )
                    .fillMaxSize()
            ) {
                SurveyList(
                    surveys = uiState.surveys,
                    onEditClick = { survey ->
                        viewModel.onEditClick(
                            survey,
                            openScreen = openDetailsScreen
                        )
                    },
                    onDeleteClick = { survey ->
                        viewModel.onDeleteButtonClick(survey)
                    },
                    onSendClick = { survey ->
                        viewModel.onSendButtonClicked(survey)
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
        if (viewModel.showSendDeleteDialog) {
            DeleteAlertDialog(
                onDeleteClick = { survey ->
                    viewModel.deleteSurvey(survey)
                },
                surveyToDelete = viewModel.deleteSurvey,
                onDismiss = {
                    viewModel.onDismissDeleteDialog()
                }
            )
        }
        if (viewModel.showSendSurveyDialog) {
            SendSurveyDialog(
                onSendClick = { survey ->
                    viewModel.sendSurvey(survey)
                },
                onCancelClick = {
                    viewModel.onDismissSendDialog()
                },
                onEmailListChanged = { viewModel.updateEmailList(it) },
                emailList = viewModel.emailList,
                selectedSurvey = viewModel.sendSurvey
            )
        }

    }
}
