package com.bme.surveysystemsupportedbyai.sentsurveydetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.Answer
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.RESPONSE_ID
import com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID
import com.bme.surveysystemsupportedbyai.surveyDetails.DetailsScreenUiState
import com.bme.surveysystemsupportedbyai.surveyDetails.DetailsScreenViewModel
import com.bme.surveysystemsupportedbyai.surveyDetails.SurveyDetailsScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentSurveyDetailsViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository
) : ViewModel(), DetailsScreenViewModel {

    private val _uiState = MutableStateFlow(SentSurveyDetailsScreenUiState())
    override val uiState: StateFlow<DetailsScreenUiState> = _uiState.asStateFlow()

    var selectedTab by mutableIntStateOf(0)
    override fun onOptionSelected(option: String) {
    }

    init {
        val responseId = savedStateHandle.get<String>(RESPONSE_ID)
        val surveyId =savedStateHandle.get<String>(SURVEY_ID)
        if (!surveyId.isNullOrEmpty()) {
            viewModelScope.launch {
                if(!responseId.isNullOrEmpty()) {
                    val response = surveysRepository.getResponse(responseId) ?: SurveyResponse()
                    _uiState.value = _uiState.value.copy(
                        response = response,
                        answers = com.bme.surveysystemsupportedbyai.surveyDetails.createAnswerMap(
                            response.answers
                        ))
                }
                val survey = surveysRepository.getSurvey(surveyId) ?: Survey()
                _uiState.value = _uiState.value.copy(survey = survey)
                surveysRepository.getResponsesWithAnswersTest(surveyId).collect{
                    responses -> _uiState.value = _uiState.value.copy(responses = responses)
                }
            }
        }
    }
    private fun createAnswerMap(answers: List<Answer?>): Map<String, Answer> {
        val answerMap = mutableStateMapOf<String, Answer>()

        for (answer in answers) {
            if(answer!=null)
                answerMap[answer.questionId] = answer
        }

        return answerMap
    }
}

data class SentSurveyDetailsScreenUiState(
    override val survey: Survey = Survey(),
    override val response: SurveyResponse = SurveyResponse(),
    override val answers: Map<String, Answer> = emptyMap(),
    val responses: List<SurveyResponse?> = emptyList()
): DetailsScreenUiState