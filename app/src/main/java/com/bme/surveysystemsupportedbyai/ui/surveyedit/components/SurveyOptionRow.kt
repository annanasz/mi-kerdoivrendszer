package com.bme.surveysystemsupportedbyai.ui.surveyedit.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.ui.surveyedit.SurveyEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyOptionRow(
    question: Question,
    viewModel: SurveyEditViewModel,
    questionIndex: Int,
    optionIndex: Int,
    option: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (question.type == "checkbox") {
            Checkbox(
                checked = false,
                onCheckedChange = { /* Handle Checkbox State */ },
                enabled = false
            )
        } else {
            RadioButton(
                selected = false,
                onClick = {},
                enabled = false,
                modifier = Modifier.padding(end = 1.dp)
            )
        }
        OutlinedTextField(
            value = option,
            onValueChange = {
                viewModel.updateQuestionOption(question, optionIndex, it)
            },
            label = { Text(text = "Option ${optionIndex + 1}") },
            modifier = Modifier
                .padding(end = 2.dp)
                .weight(1f)
        )
        IconButton(
            onClick = {
                viewModel.removeOption(question, option)
            }
        ) {
            Icon(
                imageVector = Icons.Default.RemoveCircle,
                contentDescription = "Remove Option",
                tint = Color.Red
            )
        }
    }
}