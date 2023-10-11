package com.bme.surveysystemsupportedbyai.surveyedit.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.surveyedit.SurveyEditViewModel

@Composable
fun SurveyAddOptionRow(viewModel: SurveyEditViewModel, question: Question) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
    ) {
        IconButton(
            onClick = {
                viewModel.addNewOption(question)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Option"
            )
        }
    }
}