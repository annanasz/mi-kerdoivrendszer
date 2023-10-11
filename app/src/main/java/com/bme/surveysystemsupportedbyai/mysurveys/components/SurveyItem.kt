package com.bme.surveysystemsupportedbyai.mysurveys.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import com.bme.surveysystemsupportedbyai.domain.model.SurveyRaw

@Composable
fun SurveyItem(
    survey: SurveyRaw,
    onEditClick: (SurveyRaw) -> Unit,
    onDeleteClick: (SurveyRaw) -> Unit,
    onSendClick: (SurveyRaw) -> Unit,
    onItemClick: (SurveyRaw) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black), // Adjust the color and width as needed
                shape = RoundedCornerShape(8.dp) // Adjust the shape as needed
            )
            .clickable { onItemClick(survey) }
    ) {
        Text(
            text = survey.title,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display the timestamp
            Text(
                text = survey.timestamp.toString(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
            // Display edit, delete, and send buttons as icons
            IconButton(
                onClick = { onEditClick(survey) }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray
                )
            }
            IconButton(
                onClick = { onDeleteClick(survey) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Gray
                )
            }
            IconButton(
                onClick = { onSendClick(survey) }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SurveyList(
    //surveys: State<List<Survey>>,
    surveys: List<SurveyRaw>,
    onEditClick: (SurveyRaw) -> Unit,
    onDeleteClick: (SurveyRaw) -> Unit,
    onSendClick: (SurveyRaw) -> Unit,
    onItemClick: (SurveyRaw) -> Unit
) {
    LazyColumn {
        items(surveys) { survey ->
            SurveyItem(
                survey = survey,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onSendClick = onSendClick,
                onItemClick = onItemClick
            )
        }
//        items(surveys.value, key = { it.id }) { survey ->
//            SurveyItem(
//                survey = survey,
//                onEditClick = onEditClick,
//                onDeleteClick = onDeleteClick,
//                onSendClick = onSendClick
//            )
//        }
    }
}
