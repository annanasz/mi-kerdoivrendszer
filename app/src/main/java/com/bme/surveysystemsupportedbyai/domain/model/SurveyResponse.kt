package com.bme.surveysystemsupportedbyai.domain.model

import com.google.firebase.firestore.DocumentId
import java.security.Timestamp

data class SurveyResponse(
    @DocumentId var id:String,
    val surveyId: String,
    val userId: String,
    val timestamp: Timestamp,
    val answers: List<Answer>
)