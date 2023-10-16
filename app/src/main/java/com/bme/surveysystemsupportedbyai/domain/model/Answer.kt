package com.bme.surveysystemsupportedbyai.domain.model

import com.google.firebase.firestore.DocumentId

//data class Answer(
//    val questionId: String,
//    val type: String,
//    val response: Any // This can be a String, List<String>, or Timestamp
//)
data class Answer(
    @DocumentId var id: String="",
    var questionId: String="",
    var type: String="",
    var response: List<String> = listOf()  // This can be a String, List<String>, or Timestamp
)