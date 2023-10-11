package com.bme.surveysystemsupportedbyai.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.Timestamp


data class Question(
    @DocumentId var id:String="",
    val type: String="",
    val text: String="",
    val options: List<String> = emptyList(),
    val isRequired: Boolean= true,
    var timestamp: Timestamp?=null
)