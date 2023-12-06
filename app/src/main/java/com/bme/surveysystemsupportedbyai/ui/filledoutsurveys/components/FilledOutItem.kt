package com.bme.surveysystemsupportedbyai.ui.filledoutsurveys.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun FilledOutItem(
    response: SurveyResponse,
    onItemClick: (SurveyResponse)->Unit
) {
    val dateFormat = SimpleDateFormat("yyyy. MM. dd. HH:mm:ss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val dateString = response.timestamp?.toDate()?.let { dateFormat.format(it) }
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
        .clickable { onItemClick(response) }
            .height(80.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = response.surveyTitle,
            modifier = Modifier.padding(
                top = 8.dp,
                start = 8.dp,
                end = 2.dp,
                bottom = 4.dp
            ),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = "From: ${response.senderEmail}",
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Text(
            text = "Filled out: $dateString",
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            modifier = Modifier.padding(4.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}