package com.bme.surveysystemsupportedbyai.domain.model

//data class Answer(
//    val questionId: String,
//    val type: String,
//    val response: Any // This can be a String, List<String>, or Timestamp
//)
data class Answer(
    val question: Question,
    val type: String,
    val response: Any // This can be a String, List<String>, or Timestamp
)