package com.bme.surveysystemsupportedbyai.domain.model

import com.google.firebase.firestore.DocumentId

//data class Answer(
//    val questionId: String,
//    val type: String,
//    val response: Any // This can be a String, List<String>, or Timestamp
//)
data class Answer(
    @DocumentId var id: String,
    val questionId: String,
    val type: String,
    val response: Any // This can be a String, List<String>, or Timestamp
)