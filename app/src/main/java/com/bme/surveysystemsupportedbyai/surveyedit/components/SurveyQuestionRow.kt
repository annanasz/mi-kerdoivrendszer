package com.bme.surveysystemsupportedbyai.surveyedit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.surveyedit.SurveyEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyQuestionRow(
    question: Question,
    viewModel: SurveyEditViewModel,
    questionIndex: Int
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = question.text,
                onValueChange = {
                    viewModel.updateQuestionTitle(question, it)
                },
                label = { Text(text = "Question Title") },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )

            IconButton(
                onClick = {
                    viewModel.deleteQuestion(question)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = question.isRequired,
                onCheckedChange = { isChecked ->
                    viewModel.updateQuestionIsRequired(question, isChecked)
                },
                modifier = Modifier.padding(end = 2.dp)
            )
            Text(text = "Is Required")
        }
    }
}