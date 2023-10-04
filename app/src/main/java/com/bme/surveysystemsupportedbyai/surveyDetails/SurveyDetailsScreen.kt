package com.bme.surveysystemsupportedbyai.surveyDetails

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.surveyDetails.components.QuestionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyDetailsScreen(
    viewModel: SurveyDetailsViewModel = hiltViewModel(),
    navigateBack: () -> Unit
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                itemsIndexed(survey.questions) { index, question ->
                    val questionNumber = index + 1

                    // Use QuestionItem with question number
                    QuestionItem(
                        question = question,
                        selectedOptions = emptyList(), // You can pass the selected options here
                        onOptionSelected = { option -> viewModel.onOptionSelected(option) },
                        questionNumber = questionNumber // Pass the question number
                    )
                }

            }
        }
    )
}