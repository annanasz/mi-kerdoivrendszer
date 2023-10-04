package com.bme.surveysystemsupportedbyai.surveyedit.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.domain.model.Survey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyEditTopBar(survey: Survey) {
    TopAppBar(
        title = { Text(text = survey.title) }
    )
}