package com.bme.surveysystemsupportedbyai.surveyedit.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.domain.model.Survey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyEditTopBar(
    survey: Survey,
    navigateBack: () -> Unit,
    addQuestion: () -> Unit,
    scanQuestion:()-> Unit,
    isAvailable: Boolean
) {
    TopAppBar(
        title = { Text(text = survey.title) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { addQuestion() }, enabled = isAvailable) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Question")
            }
            IconButton(onClick = { scanQuestion() }, enabled = isAvailable) {
                Icon(imageVector = Icons.Default.CameraEnhance, contentDescription ="Scan questions")
            }
        }
    )
}