package com.bme.surveysystemsupportedbyai.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Survey(
    @DocumentId val id:String="",
    val creatorId: String="",
    val title: String="",
    val questions: List<Question> = emptyList(),
    val timestamp: Timestamp?=null,
    val responseIds: List<String>?= listOf()
)