package com.bme.surveysystemsupportedbyai.inboxsurveys.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey

@Composable
fun InboxSurveyItem(
    survey: ReceivedSurvey,
    onFillOutClick: (ReceivedSurvey) -> Unit,
    onFillOutWithSpeechClick: (ReceivedSurvey) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = survey.surveyTitle,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            IconButton(
                onClick = {
                    onFillOutClick(survey)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.EditNote,
                    contentDescription = "FillOut",
                    tint = Color.Black
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
                    tint = Color.Black
                )
            }
        }
        Text(
            text = "From: ${survey.senderEmail}",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = survey.timestamp?.toDate().toString(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

