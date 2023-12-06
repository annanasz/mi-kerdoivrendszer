package com.bme.surveysystemsupportedbyai.ui.sentsurveys.components

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
import com.bme.surveysystemsupportedbyai.domain.model.SentSurvey
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun SentSurveyItem(
    survey: SentSurvey,
    openDetailsScreen: (String) -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy. MM. dd. HH:mm:ss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val dateString = survey.timestamp?.toDate()?.let { dateFormat.format(it) }
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .clickable { openDetailsScreen(survey.surveyId) }
            .height(80.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = survey.surveyTitle,
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    start = 8.dp,
                    end = 2.dp,
                    bottom = 16.dp
                ),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sent: $dateString",
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}