package com.bme.surveysystemsupportedbyai.ui.filloutwithspeech.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyAlertDialog(
    onOkClick: () -> Unit,
    onCancelClick: () -> Unit,
    onApiKeyChanged: (String) -> Unit,
    apiKey: String,
    isApiKey: Boolean,
    isLoading: MutableState<Boolean>,
    deleteApiKey: () -> Unit,
    apiKeyText: MutableState<String>
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Dialog(
        onDismissRequest = {},
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(fontWeight = FontWeight.Bold, fontSize = 20.sp, text = "OpenAI API key")
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    fontSize = 14.sp,
                    text = "You have to provide a valid API key to use this functionality!"
                )
                Box(modifier = Modifier
                    .height(height = 56.dp)
                    .fillMaxWidth()) {
                    if (!isLoading.value)
                        Text(apiKeyText.value)
                    else
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp)
                        )
                }

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = {
                        onApiKeyChanged(it)
                    },
                    singleLine = false,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { onCancelClick() }
                    ) {
                        Text(text = "Cancel")
                    }
                    TextButton(
                        onClick = {
                            deleteApiKey()
                        },
                        enabled = isApiKey
                    ) {
                        Text(text = "Delete Api Key")
                    }

                    Button(
                        onClick = {
                            onOkClick()
                        },
                        enabled = apiKey.isNotEmpty()
                    ) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}