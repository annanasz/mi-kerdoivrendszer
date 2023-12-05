package com.bme.surveysystemsupportedbyai.ui.inboxsurveys.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun InboxSurveyItem(
    survey: ReceivedSurvey,
    onFillOutClick: (ReceivedSurvey) -> Unit,
    onFillOutWithSpeechClick: (ReceivedSurvey) -> Unit,
) {
    val dateFormat = SimpleDateFormat("yyyy. MM. dd. HH:mm:ss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val dateString = survey.timestamp?.toDate()?.let { dateFormat.format(it) }
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .height(80.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = survey.surveyTitle,
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        start = 8.dp,
                        end = 2.dp,
                        bottom =0.dp
                    )
                    .weight(1f),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            IconButton(
                onClick = {
                    onFillOutClick(survey)
                },
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EditNote,
                    contentDescription = "FillOut",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.dp).size(22.dp)
                )
            }
            IconButton(
                onClick = {
                    onFillOutWithSpeechClick(survey)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "FillOutWithSpeech",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.dp).size(22.dp)
                )
            }
        }
        Text(
            text = "From: ${survey.senderEmail}",
            modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.dp),
            fontSize = 12.sp
        )
        Text(
            text = dateString.toString(),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

