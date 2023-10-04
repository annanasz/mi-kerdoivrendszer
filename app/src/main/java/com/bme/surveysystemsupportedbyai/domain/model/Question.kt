package com.bme.surveysystemsupportedbyai.domain.model

data class Question(
    val type: String,
    val text: String,
    val options: List<String>,
    val isRequired: Boolean
)