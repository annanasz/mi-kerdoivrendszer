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
import com.bme.surveysystemsupportedbyai.domain.model.Survey

@Composable
fun SurveyItem(
    survey: Survey,
    onEditClick: (Survey) -> Unit,
    onDeleteClick: (Survey) -> Unit,
    onSendClick: (Survey) -> Unit,
    onItemClick: (Survey) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(horizontal = 2.dp, vertical = 2.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onItemClick(survey) }
    ) {
        Text(
            text = survey.title,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth().padding(0.dp)
        ) {
            IconButton(
                onClick = { onEditClick(survey) },
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray,
                    modifier = Modifier.padding(4.dp)
                )
            }
            IconButton(
                onClick = { onDeleteClick(survey) },
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Gray,
                    modifier = Modifier.padding(4.dp)
                )
            }
            IconButton(
                onClick = { onSendClick(survey) },
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.Gray,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Display the timestamp
            Text(
                text = survey.timestamp?.toDate().toString(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

    }
}


@Composable
fun SurveyList(
    //surveys: State<List<Survey>>,
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
