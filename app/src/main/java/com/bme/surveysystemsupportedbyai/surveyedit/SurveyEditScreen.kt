package com.bme.surveysystemsupportedbyai.surveyedit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.surveyedit.components.SurveyEditTopBar
import com.bme.surveysystemsupportedbyai.surveyedit.components.SurveyQuestionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyEditScreen(viewModel: SurveyEditViewModel = hiltViewModel()) {
    val survey by viewModel.survey

    Scaffold(
        topBar = { SurveyEditTopBar(survey = survey) },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                itemsIndexed(survey.questions) { index, question ->
                    SurveyQuestionCard(
                        question = question,
                        viewModel = viewModel,
                        questionIndex = index
                    )
                }
            }
        }
    )
}
