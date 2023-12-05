package com.bme.surveysystemsupportedbyai.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId


data class SurveyResponse(
    @DocumentId var id:String="",
    var surveyId: String = "",
    var surveyTitle: String ="",
    var userId: String ="",
    var userEmail: String = "",
    var senderEmail: String ="",
    var timestamp: Timestamp?=null,
    var answers: List<Answer?> = listOf()
)