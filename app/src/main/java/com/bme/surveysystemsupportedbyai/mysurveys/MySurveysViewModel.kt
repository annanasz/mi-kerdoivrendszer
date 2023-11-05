package com.bme.surveysystemsupportedbyai.mysurveys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey
import com.bme.surveysystemsupportedbyai.domain.model.SentSurvey
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MySurveysViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val surveysRepository: SurveysRepository
) : ViewModel() {
    val surveys = surveysRepository.userSurveys

    fun signOut() = authRepository.signOut()
    fun onEditClick(survey: Survey, openScreen: (String) -> Unit) {
        openScreen("${Screen.SurveyEditScreen.route}?$SURVEY_ID={${survey.id}}")
    }

    fun onDeleteClick(survey: Survey) {
        viewModelScope.launch {
            surveysRepository.deleteSurvey(survey.id)
        }
    }

    fun onItemClick(survey: Survey, openScreen: (String) -> Unit) {
        val responseNeeded = ""
        openScreen("${Screen.SurveyDetailsScreen.route}?$SURVEY_ID=${survey.id}&$RESPONSE_ID=${responseNeeded}")
    }

    fun onSendSurveyClick(survey: Survey?, emails: String) {
        if (survey == null)
            return
        val emailsList = emails.split(',').map { it.trim() }
        val sentSurvey = SentSurvey(
            surveyId = survey.id,
            surveyTitle=survey.title,
            senderId = authRepository.currentUser?.uid,
            senderEmail = authRepository.currentUser?.email,
            recipients = emailsList
        )
        val receivedSurveysList = mutableListOf<ReceivedSurvey>()
        for (email: String in emailsList) {
            val receivedSurvey = ReceivedSurvey(
                surveyId = survey.id,
                surveyTitle = survey.title,
                senderEmail = authRepository.currentUser?.email,
                recipientEmail = email
            )
            receivedSurveysList.add(receivedSurvey)
        }
        viewModelScope.launch {
            surveysRepository.saveSentSurvey(sentSurvey)
            surveysRepository.saveReceivedSurvey(receivedSurveysList)
        }
    }

}

const val SURVEY_ID = "surveyId"
const val RESPONSE_ID="responseId"