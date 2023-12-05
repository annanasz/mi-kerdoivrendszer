package com.bme.surveysystemsupportedbyai.ui.filledoutsurveys.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse

@Composable
fun FilledOutList(
    responses: List<SurveyResponse>,
    onItemClick: (SurveyResponse)->Unit
){
    LazyColumn {
        items(responses) { response ->
            FilledOutItem(
                response = response,
                onItemClick= onItemClick
            )
        }
    }
}