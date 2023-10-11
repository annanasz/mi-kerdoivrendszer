package com.bme.surveysystemsupportedbyai.domain.repository
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey
import com.bme.surveysystemsupportedbyai.domain.model.SentSurvey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyRaw
import kotlinx.coroutines.flow.Flow

interface SurveysRepository {
    val userSurveys: Flow<List<SurveyRaw>>
    suspend fun getSurvey(surveyId:String): SurveyRaw?
    suspend fun saveSurvey(survey: SurveyRaw)
    suspend fun updateSurvey(survey: SurveyRaw)
    suspend fun deleteSurvey(surveyId: String)
    suspend fun saveSentSurvey(sentSurvey: SentSurvey)
    val sentSurveys: Flow<List<SentSurvey>>
    suspend fun saveReceivedSurvey(receivedSurveys: List<ReceivedSurvey>)
    val receivedSurveys: Flow<List<ReceivedSurvey>>
    val filledOutSurveys: Flow<List<SurveyRaw>>
}