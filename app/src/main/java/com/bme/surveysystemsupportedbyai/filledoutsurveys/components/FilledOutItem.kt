package com.bme.surveysystemsupportedbyai.filledoutsurveys.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse

@Composable
fun FilledOutItem(
    response: SurveyResponse
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            )
        //.clickable { onItemClick(survey) }
    ) {
        Text(
            text = response.surveyTitle,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Filled out: ${response.timestamp?.toDate().toString()}",
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}