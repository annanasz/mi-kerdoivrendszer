package com.bme.surveysystemsupportedbyai.domain.model


import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId


data class ReceivedSurvey(
    @DocumentId var id:String="",
    val surveyId: String? ="",
    val surveyTitle: String="",
    val senderEmail: String?="",
    val recipientEmail: String?="",
    val filled:Boolean=false,
    var timestamp: Timestamp?=null
)