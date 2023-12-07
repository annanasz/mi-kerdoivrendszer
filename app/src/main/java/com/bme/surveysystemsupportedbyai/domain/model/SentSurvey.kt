package com.bme.surveysystemsupportedbyai.domain.model


import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class SentSurvey(
    @DocumentId var id:String="",
    val surveyId: String ="",
    val surveyTitle: String = "",
    val senderId: String?="",
    val senderEmail: String?="",
    val recipients: List<String> = emptyList(),
    val timestamp: Timestamp? = null
)