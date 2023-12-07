package com.bme.surveysystemsupportedbyai.ui.mysurveys

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey
import com.bme.surveysystemsupportedbyai.domain.model.SentSurvey
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MySurveysViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val surveysRepository: SurveysRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MySurveysUiState())
    val uiState: StateFlow<MySurveysUiState> = _uiState.asStateFlow()
    val loading = mutableStateOf(true)


    var emailList by mutableStateOf("")
        private set
    var deleteSurvey: Survey? by  mutableStateOf(null)
        private set
    var sendSurvey: Survey? by mutableStateOf(null)
        private set
    var showSendSurveyDialog by mutableStateOf(false)
        private set
    var showSendDeleteDialog by mutableStateOf(false)
        private set

    fun fetchData() {
        viewModelScope.launch {
            try {
                surveysRepository.userSurveys.collect { surveys ->
                    _uiState.value = _uiState.value.copy(surveys = surveys)
                    loading.value = false
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
                loading.value = false
            }
        }
    }

    fun updateEmailList(givenEmailList:String){
        emailList = givenEmailList
    }
    fun signOut() = authRepository.signOut()
    fun onEditClick(survey: Survey, openScreen: (String) -> Unit) {
        openScreen("${Screen.SurveyEditScreen.route}?$SURVEY_ID={${survey.id}}")
    }

    fun onDeleteButtonClick(surveyToDelete: Survey){
        showSendDeleteDialog = true
        deleteSurvey = surveyToDelete
    }

    fun deleteSurvey(survey: Survey) {
        viewModelScope.launch {
            surveysRepository.deleteSurvey(survey.id)
        }
    }

    fun onDismissDeleteDialog(){
        deleteSurvey = null
        showSendDeleteDialog = false
    }

    fun onDismissSendDialog(){
        sendSurvey = null
        showSendSurveyDialog = false
    }

    fun onItemClick(survey: Survey, openScreen: (String) -> Unit) {
        val responseNeeded = ""
        openScreen("${Screen.SurveyDetailsScreen.route}?$SURVEY_ID=${survey.id}&$RESPONSE_ID=${responseNeeded}")
    }

    fun onSendButtonClicked(surveyToSend: Survey){
        showSendSurveyDialog = true
        sendSurvey = surveyToSend
    }
    fun sendSurvey(survey: Survey?) {
        if (survey == null)
            return
        val emailsList = emailList.split(',').map { it.trim() }
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
        showSendSurveyDialog=false
    }
}

data class MySurveysUiState(
    val surveys: List<Survey> = emptyList(),
    val errorMessage: String? = null
)

const val SURVEY_ID = "surveyId"
const val RESPONSE_ID="responseId"