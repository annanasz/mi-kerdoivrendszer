package com.bme.surveysystemsupportedbyai.sentsurveys.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.domain.model.SentSurvey

@Composable
fun SentSurveyList(
    surveys: List<SentSurvey>,
    openDetailsScreen: (String) -> Unit
) {
    LazyColumn {
        items(surveys) { survey ->
            SentSurveyItem(
                survey = survey,
                openDetailsScreen = openDetailsScreen
            )
        }
    }
}