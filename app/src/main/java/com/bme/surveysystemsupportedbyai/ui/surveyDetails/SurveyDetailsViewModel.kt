package com.bme.surveysystemsupportedbyai.ui.surveyDetails

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.Answer
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.RESPONSE_ID
import com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository
) : ViewModel(), DetailsViewModel {

    private val _uiState = MutableStateFlow(SurveyDetailsScreenUiState())
    override val uiState: StateFlow<DetailsScreenUiState> = _uiState.asStateFlow()

    init {
        val responseId = savedStateHandle.get<String>(RESPONSE_ID)
        val surveyId = savedStateHandle.get<String>(SURVEY_ID)
        if (!surveyId.isNullOrEmpty()) {
            viewModelScope.launch {
                if (!responseId.isNullOrEmpty()) {
                    val response = surveysRepository.getResponse(responseId) ?: SurveyResponse()
                    _uiState.value = _uiState.value.copy(
                        response = response,
                        answers = createAnswerMap(response.answers)
                    )
                }
                val survey = surveysRepository.getSurvey(surveyId) ?: Survey()
                _uiState.value = _uiState.value.copy(survey = survey)
            }
        }
    }


    override fun onOptionSelected(option: String) {

    }
}

fun String.idFromParameter(): String {
    return this.substring(1, this.length - 1)
}

fun createAnswerMap(answers: List<Answer?>): Map<String, Answer> {
    val answerMap = mutableStateMapOf<String, Answer>()

    for (answer in answers) {
        if (answer != null)
            answerMap[answer.questionId] = answer
    }

    return answerMap
}

data class SurveyDetailsScreenUiState(
    override val survey: Survey = Survey(),
    override val response: SurveyResponse = SurveyResponse(),
    override val answers: Map<String, Answer> = emptyMap()
) : DetailsScreenUiState

interface DetailsViewModel {
    val uiState: StateFlow<DetailsScreenUiState>
    fun onOptionSelected(option: String)
}

interface DetailsScreenUiState {
    val survey: Survey
    val response: SurveyResponse
    val answers: Map<String, Answer>
}