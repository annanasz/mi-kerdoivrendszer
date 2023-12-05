package com.bme.surveysystemsupportedbyai.domain.repository
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey
import com.bme.surveysystemsupportedbyai.domain.model.SentSurvey
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import kotlinx.coroutines.flow.Flow

interface SurveysRepository {
    val userSurveys: Flow<List<Survey>>
    val sentSurveys: Flow<List<SentSurvey>>
    val receivedSurveys: Flow<List<ReceivedSurvey>>
    val filledOutSurveys: Flow<List<SurveyResponse>>
    suspend fun getSurvey(surveyId:String): Survey?
    suspend fun saveSurvey(survey: Survey): String?
    suspend fun updateSurvey(survey: Survey)
    suspend fun deleteSurvey(surveyId: String)
    suspend fun saveSentSurvey(sentSurvey: SentSurvey)
    suspend fun saveReceivedSurvey(receivedSurveys: List<ReceivedSurvey>)
    suspend fun updateReceivedSurvey(receivedSurvey: String)
    suspend fun fillOutSurvey(surveyResponse: SurveyResponse):Boolean
    suspend fun getResponse(responseId: String): SurveyResponse?
    suspend fun getResponsesWithAnswers(surveyId: String): List<SurveyResponse?>
    suspend fun getResponsesWithAnswersTest(surveyId: String): Flow<List<SurveyResponse?>>
}