package com.bme.surveysystemsupportedbyai.domain.model

import com.google.firebase.firestore.DocumentId

//data class Survey(
//    val creatorId: String,
//    val title: String,
//    val questions: List<Question>,
//    val timestamp: Timestamp,
//    val responseIds: List<String>)
data class Survey(
    @DocumentId val id:String="",
    val creator: User = User(),
    val title: String ="",
    val questions: List<Question> = emptyList(),
    val timestamp: String = "",
    val responses: List<SurveyResponse> = emptyList()
)
