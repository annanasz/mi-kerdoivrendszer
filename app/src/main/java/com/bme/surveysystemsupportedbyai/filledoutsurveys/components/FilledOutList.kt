package com.bme.surveysystemsupportedbyai.filledoutsurveys.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse

@Composable
fun FilledOutList(
    responses: List<SurveyResponse>
){
    LazyColumn {
        items(responses) { response ->
            FilledOutItem(
                response = response
            )
        }
    }
}