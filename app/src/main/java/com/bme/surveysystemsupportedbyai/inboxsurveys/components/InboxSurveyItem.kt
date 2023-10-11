package com.bme.surveysystemsupportedbyai.inboxsurveys.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
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
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black), // Adjust the color and width as needed
                shape = RoundedCornerShape(8.dp) // Adjust the shape as needed
            )
        //.clickable { onItemClick(survey) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = survey.surveyTitle ?: "Untitled", // Add a default title if it's null
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f), // Use weight to make the title expand to the left
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            // Edit icon button
            IconButton(
                onClick = {
                    onFillOutClick(survey)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.EditNote,
                    contentDescription = "Edit",
                    tint = Color.Black // Customize the icon color as needed
                )
            }
        }
        Text(
            text = "From: ${survey.senderEmail}",
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = survey.timestamp?.toDate().toString(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

