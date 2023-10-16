package com.bme.surveysystemsupportedbyai.domain.repository
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey
import com.bme.surveysystemsupportedbyai.domain.model.SentSurvey
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import kotlinx.coroutines.flow.Flow

interface SurveysRepository {
    val userSurveys: Flow<List<Survey>>
    suspend fun getSurvey(surveyId:String): Survey?
    suspend fun saveSurvey(survey: Survey): String?
    suspend fun updateSurvey(survey: Survey)
    suspend fun deleteSurvey(surveyId: String)
    suspend fun saveSentSurvey(sentSurvey: SentSurvey)
    val sentSurveys: Flow<List<SentSurvey>>
    suspend fun saveReceivedSurvey(receivedSurveys: List<ReceivedSurvey>)
    suspend fun fillOutSurvey(surveyResponse: SurveyResponse):Boolean
    val receivedSurveys: Flow<List<ReceivedSurvey>>
    val filledOutSurveys: Flow<List<SurveyResponse>>
}