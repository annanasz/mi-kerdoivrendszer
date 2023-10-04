package com.bme.surveysystemsupportedbyai.domain.model

import java.security.Timestamp

//data class ReceivedSurvey(
//    val surveyId: String,
//    val senderId: String,
//    val recipientId: String,
//    val timestamp: Timestamp
//)

data class ReceivedSurvey(
    val survey: Survey,
    val sender: User,
    val recipient: User,
    val timestamp: Timestamp
)