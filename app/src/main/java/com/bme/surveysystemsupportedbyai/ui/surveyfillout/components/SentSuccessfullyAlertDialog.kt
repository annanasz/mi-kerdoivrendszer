package com.bme.surveysystemsupportedbyai.ui.surveyfillout.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SentSuccessfullyAlertDialog(
    onDismiss: ()->Unit,
    navigateBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
            navigateBack()
        },
        title = {
            Text(text = "Survey Response Successfully Sent")
        },
        text = {
            Text(text = "Your survey response has been sent.")
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    navigateBack()
                }
            ) {
                Text(text = "OK")
            }
        }
    )
}