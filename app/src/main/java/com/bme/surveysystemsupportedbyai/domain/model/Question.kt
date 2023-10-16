package com.bme.surveysystemsupportedbyai.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.Timestamp


data class Question(
    @DocumentId var id:String="",
    var type: String="",
    var text: String="",
    var options: List<String> = mutableListOf(),
    var isRequired: Boolean= true,
    var timestamp: Timestamp?=null
)