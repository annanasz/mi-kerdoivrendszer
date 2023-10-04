package com.bme.surveysystemsupportedbyai.domain.model

import java.security.Timestamp

//data class SentSurvey(
//    val surveyId: String,
//    val senderId: String,
//    val recipientIds: List<String>,
//    val timestamp: Timestamp
//)

data class SentSurvey(
    val survey: Survey,
    val sender: User,
    val recipients: List<User>,
    val timestamp: Timestamp
)