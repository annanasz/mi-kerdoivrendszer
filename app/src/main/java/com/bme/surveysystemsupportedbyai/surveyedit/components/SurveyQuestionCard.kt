package com.bme.surveysystemsupportedbyai.surveyedit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.surveyedit.SurveyEditViewModel

@Composable
fun SurveyQuestionCard(
    question: Question,
    viewModel: SurveyEditViewModel,
    questionIndex: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SurveyQuestionRow(
                question = question,
                viewModel = viewModel,
                questionIndex = questionIndex
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (question.type == "checkbox" || question.type == "multiple_choice") {
                question.options.forEachIndexed { optionIndex, option ->
                    SurveyOptionRow(
                        question = question,
                        viewModel = viewModel,
                        questionIndex = questionIndex,
                        optionIndex = optionIndex,
                        option = option
                    )
                }

                SurveyAddOptionRow(viewModel, question)
            }
        }
    }
}