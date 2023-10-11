package com.bme.surveysystemsupportedbyai.inboxsurveys.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey

@Composable
fun InboxSurveyList(
    surveys: List<ReceivedSurvey>,
    onFillOutClick: (ReceivedSurvey) -> Unit,
) {
    LazyColumn {
        items(surveys) { survey ->
            InboxSurveyItem(
                survey = survey,
                onFillOutClick = onFillOutClick
            )
        }
    }
}