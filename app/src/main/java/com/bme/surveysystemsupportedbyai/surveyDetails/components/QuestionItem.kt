package com.bme.surveysystemsupportedbyai.surveyDetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bme.surveysystemsupportedbyai.domain.model.Answer
import com.bme.surveysystemsupportedbyai.domain.model.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionItem(
    question: Question,
    selectedOptions: List<String>,
    onOptionSelected: (String) -> Unit,
    questionNumber: Int,
    response: Answer? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$questionNumber. ${question.text}",
                fontWeight = FontWeight.Bold
            )

            if (question.isRequired) {
                Text(
                    text = " *",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        when (question.type) {
            "multiple_choice" -> {
                question.options.forEach { option ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = response?.response?.contains(option) == true,
                            onClick = { onOptionSelected(option) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = option)
                    }
                }
            }
            "checkbox" -> {
                question.options.forEach { option ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = response?.response?.contains(option) == true,
                            onCheckedChange = { isChecked ->
                                if (isChecked) onOptionSelected(option)
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = option)
                    }
                }
            }
            "short_answer" -> {
                OutlinedTextField(
                    value = response?.response?.getOrNull(0) ?:"Response here",
                    onValueChange = { /* Nothing to do here as it's read-only */ },
                    enabled = false,
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}