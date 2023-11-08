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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.domain.model.Answer
import com.bme.surveysystemsupportedbyai.filloutwithspeech.LoadingAnimation
import com.bme.surveysystemsupportedbyai.surveyDetails.components.QuestionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyDetailsScreen(
    navigateBack: () -> Unit,
    viewModel: SurveyDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = uiState.survey.title)
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            SurveyDetailsContent(uiState, {option -> viewModel.onOptionSelected(option)} , padding = padding)
        }
    )
}

@Composable
fun SurveyDetailsContent(uiState:DetailsScreenUiState, onOptionSelected: (String) -> Unit ,padding:PaddingValues) {
    if (uiState.survey.id.isEmpty())
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
            itemsIndexed(uiState.survey.questions) { index, question ->
                val questionNumber = index + 1
                val answer: Answer? = uiState.answers[question.id]

                QuestionItem(
                    question = question,
                    selectedOptions = emptyList(),
                    onOptionSelected = onOptionSelected,
                    questionNumber = questionNumber,
                    response = answer
                )
            }
        }
    }
}

