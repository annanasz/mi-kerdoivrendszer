package com.bme.surveysystemsupportedbyai.domain.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class SurveyRaw(
    @DocumentId val id:String="",
    val creatorId: String="",
    val title: String="",
    val questions: List<Question> = emptyList(),
    val timestamp: Date?=null,
    val responseIds: List<String>?= emptyList()
)
data class Survey(
    @DocumentId val id:String="",
    val creator: User = User(),
    val title: String ="",
    val questions: List<Question> = emptyList(),
    val timestamp: String = "",
    val responses: List<SurveyResponse> = emptyList()
)