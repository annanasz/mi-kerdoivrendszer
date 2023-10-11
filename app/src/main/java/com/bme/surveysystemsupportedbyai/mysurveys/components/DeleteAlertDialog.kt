package com.bme.surveysystemsupportedbyai.mysurveys.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.bme.surveysystemsupportedbyai.domain.model.SurveyRaw

@Composable
fun DeleteAlertDialog(
    onDeleteClick: (SurveyRaw) -> Unit,
    surveyToDelete: SurveyRaw?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

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