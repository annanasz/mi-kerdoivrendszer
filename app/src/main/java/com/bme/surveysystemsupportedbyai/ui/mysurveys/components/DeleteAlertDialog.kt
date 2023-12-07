package com.bme.surveysystemsupportedbyai.ui.mysurveys.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.domain.model.Survey

@Composable
fun DeleteAlertDialog(
    onDeleteClick: (Survey) -> Unit,
    surveyToDelete: Survey?,
    onDismiss: () -> Unit
) {

    if (surveyToDelete != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Delete Survey")
            },
            text = {
                Text("Are you sure you want to delete this survey?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick(surveyToDelete)
                        onDismiss()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}