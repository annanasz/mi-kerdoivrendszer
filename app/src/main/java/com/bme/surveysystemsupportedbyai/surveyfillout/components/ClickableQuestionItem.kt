package com.bme.surveysystemsupportedbyai.surveyfillout.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bme.surveysystemsupportedbyai.domain.model.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableQuestionItem(
    question: Question,
    selectedOptions: Set<String>,
    onOptionSelected: (String, String) -> Unit,
    onOptionDeselected: (String, String) -> Unit,
    questionNumber: Int
) {
    var localSelectedOptions by remember { mutableStateOf(selectedOptions) }
    var shortAnswerText by remember { mutableStateOf("") }
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
                            selected = localSelectedOptions.contains(option),
                            onClick = {
                                localSelectedOptions = setOf(option)
                                onOptionSelected(question.id, option)
                            },
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
                            checked = localSelectedOptions.contains(option),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    onOptionSelected(question.id, option)
                                    localSelectedOptions += option
                                } else {
                                    onOptionDeselected(question.id, option)
                                    localSelectedOptions -= option
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = option)
                    }
                }
            }

            "short_answer" -> {
                OutlinedTextField(
                    value = shortAnswerText,
                    onValueChange = {
                        shortAnswerText = it
                        onOptionSelected(question.id, it)
                    },
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}
