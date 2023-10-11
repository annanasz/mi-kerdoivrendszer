package com.bme.surveysystemsupportedbyai.surveyedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.surveyedit.components.AddQuestionDialog
import com.bme.surveysystemsupportedbyai.surveyedit.components.SurveyEditTopBar
import com.bme.surveysystemsupportedbyai.surveyedit.components.SurveyQuestionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyEditScreen(
    viewModel: SurveyEditViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val survey by viewModel.survey

    var isDialogOpen by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        SurveyEditTopBar(survey = survey,
            navigateBack = { navigateBack() },
            addQuestion = { isDialogOpen = true })
    }, content = { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(survey.questions) { index, question ->
                    SurveyQuestionCard(
                        question = question, viewModel = viewModel, questionIndex = index
                    )
                }
            }

            FloatingActionButton(
                onClick = {
                    viewModel.onDoneClick(navigateBack)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .width(132.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "DONE", fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
                }
            }
        }
    })
    if (isDialogOpen) {
        AddQuestionDialog(
            onAddQuestion = { questionText ->
                viewModel.addQuestion(questionText)
            },
            onDismiss = { isDialogOpen = false },
            onCancel = { isDialogOpen = false }
        )
    }
}