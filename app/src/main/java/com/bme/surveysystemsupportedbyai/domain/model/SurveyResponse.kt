package com.bme.surveysystemsupportedbyai.domain.model

import java.security.Timestamp

data class SurveyResponse(
    val survey: Survey,
    val user: User,
    val timestamp: Timestamp,
    val answers: List<Answer>
)