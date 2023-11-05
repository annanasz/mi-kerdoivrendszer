package com.bme.surveysystemsupportedbyai.surveyDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.domain.model.Answer
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.filloutwithspeech.LoadingAnimation
import com.bme.surveysystemsupportedbyai.surveyDetails.components.QuestionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyDetailsScreen(
    navigateBack: () -> Unit,
    viewModel: SurveyDetailsViewModel = hiltViewModel()
) {
    val survey by viewModel.survey
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = survey.title)
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            SurveyDetailsContent(viewModel , padding = padding)
        }
    )
}

@Composable
fun SurveyDetailsContent(viewModel:DetailsScreenViewModel, padding:PaddingValues) {
    val survey by viewModel.survey
    val response by viewModel.response
    val answers by viewModel.answers

    if (survey.id.isEmpty())
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            LoadingAnimation()
        } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            itemsIndexed(survey.questions) { index, question ->
                val questionNumber = index + 1
                val answer: Answer? = answers[question.id]

                QuestionItem(
                    question = question,
                    selectedOptions = emptyList(),
                    onOptionSelected = { option -> viewModel.onOptionSelected(option) },
                    questionNumber = questionNumber,
                    response = answer
                )
            }
        }
    }
}

interface DetailsScreenViewModel{
    val survey: MutableState<Survey>
    val response: MutableState<SurveyResponse>
    val answers: MutableState<Map<String, Answer>>
    fun onOptionSelected(option:String)
}