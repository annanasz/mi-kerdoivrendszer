package com.bme.surveysystemsupportedbyai.surveyedit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuestionDialog(
    onAddQuestion: (String) -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    var questionText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("multiple_choice") } // Default type
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val questionTypes = listOf("multiple_choice", "checkbox", "short_answer")
    val dialogShape: Shape = CircleShape

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .size(300.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = "Add a new question")
                Spacer(modifier = Modifier.height(32.dp))

                Column { // Wrap the "Type" text field and dropdown menu in a Column
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Type: ",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        BasicTextField(
                            value = selectedType,
                            onValueChange = { selectedType = it },
                            readOnly = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                                .clickable { isDropdownExpanded = !isDropdownExpanded }
                        )
                        IconButton(
                            onClick = { isDropdownExpanded = !isDropdownExpanded },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand"
                            )
                        }
                    }

                    if (isDropdownExpanded) {
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray)
                        ) {
                            questionTypes.forEach { type ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedType = type
                                        isDropdownExpanded = false
                                    },
                                    text = {
                                        Text(text = type)
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onCancel()
                            onDismiss()
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            onAddQuestion(selectedType)
                            onDismiss()
                        }
                    ) {
                        Text(text = "Add")
                    }
                }
            }
        }
    }
}