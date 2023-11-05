package com.bme.surveysystemsupportedbyai.surveyfillout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.filloutwithspeech.LoadingAnimation
import com.bme.surveysystemsupportedbyai.surveyfillout.components.ClickableQuestionItem
import com.bme.surveysystemsupportedbyai.surveyfillout.components.SentSuccessfullyAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyFillOutScreen(
    viewModel: SurveyFillOutViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val survey by viewModel.survey
    val isSendButtonEnabled = viewModel.allRequiredQuestionsAnswered()
    val isSentAlertDialogVisible = viewModel.done
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (survey.id.isNotEmpty())"Fill out ${survey.title}" else "")
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->

            if (survey.id.isEmpty()) Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation()
            } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                itemsIndexed(survey.questions) { index, question ->
                    val questionNumber = index + 1
                    ClickableQuestionItem(
                        question = question,
                        selectedOptions = setOf(),
                        onOptionSelected = { questionId, option ->
                            viewModel.updateSelectedOption(
                                questionId,
                                option
                            )
                        },
                        onOptionDeselected = { questionId, option ->
                            viewModel.removeSelectedOption(
                                questionId,
                                option
                            )
                        },
                        questionNumber = questionNumber
                    )
                }
                if (survey.id != "") {
                    if (!isSendButtonEnabled)
                        item {
                            Text(text = "Fill out all required fields to send the survey!")
                        }
                    item {
                        Button(
                            onClick = {
                                viewModel.sendFilledOutSurvey()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            enabled = isSendButtonEnabled
                        ) {
                            Text(text = "Send")
                        }
                    }
                }

            }
        }
        }
    )
    if(isSentAlertDialogVisible.value){
         SentSuccessfullyAlertDialog(onDismiss = {  }, navigateBack = navigateBack)
    }
}