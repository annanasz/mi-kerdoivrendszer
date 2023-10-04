package com.bme.surveysystemsupportedbyai.domain.model

data class User(
    val email: String="",
    val surveys: List<Survey> = emptyList()
)