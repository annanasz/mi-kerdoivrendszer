package com.bme.surveysystemsupportedbyai.ui.mysurveys.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.ui.text.font.FontWeight
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun SurveyItem(
    survey: Survey,
    onEditClick: (Survey) -> Unit,
    onDeleteClick: (Survey) -> Unit,
    onSendClick: (Survey) -> Unit,
    onItemClick: (Survey) -> Unit
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
            .clickable { onItemClick(survey) }
            .height(80.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            Text(
                text = survey.title,
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        start = 8.dp,
                        end = 2.dp,
                        bottom = 16.dp
                    )
                    .weight(1f),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,

                )

            IconButton(
                onClick = { onEditClick(survey) },
                modifier = Modifier.padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.dp)
                )
            }
            IconButton(
                onClick = { onDeleteClick(survey) },
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.dp)
                )
            }
            IconButton(
                onClick = { onSendClick(survey) },
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.dp)
                )

            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = dateString ?: "",
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )
    }
}


@Composable
fun SurveyList(
    surveys: List<Survey>,
    onEditClick: (Survey) -> Unit,
    onDeleteClick: (Survey) -> Unit,
    onSendClick: (Survey) -> Unit,
    onItemClick: (Survey) -> Unit
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
    }
}
