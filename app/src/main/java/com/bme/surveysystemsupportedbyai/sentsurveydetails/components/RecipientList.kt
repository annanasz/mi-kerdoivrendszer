package com.bme.surveysystemsupportedbyai.sentsurveydetails.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse

@Composable
fun RecipientList(
    responses: List<SurveyResponse?>,
    openDetailsScreen: (String, String) -> Unit
) {
    LazyColumn {
        items(responses) { response ->
            RecipientItem(
                response = response,
                openDetailsScreen = openDetailsScreen
            )
        }
    }
}