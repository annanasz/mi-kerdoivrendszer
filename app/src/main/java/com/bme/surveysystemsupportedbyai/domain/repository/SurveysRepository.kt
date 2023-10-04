package com.bme.surveysystemsupportedbyai.domain.repository

import com.bme.surveysystemsupportedbyai.domain.model.Survey
import kotlinx.coroutines.flow.Flow

interface SurveysRepository {
    val userSurveys: Flow<List<Survey>>
    suspend fun getSurvey(surveyId:String): Survey?
}