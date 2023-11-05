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
import com.bme.surveysystemsupportedbyai.surveyDetails.DetailsScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentSurveyDetailsViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository
) : ViewModel(), DetailsScreenViewModel {
    var selectedTab by mutableIntStateOf(0)
    override val survey = mutableStateOf(Survey())
    override val answers = mutableStateOf(emptyMap<String, Answer>())
    var responsesTest: Flow<List<SurveyResponse?>> = flowOf(emptyList())
    override fun onOptionSelected(option: String) {
    }

     override val response: MutableState<SurveyResponse> = mutableStateOf(SurveyResponse())

    init {
        val responseId = savedStateHandle.get<String>(RESPONSE_ID)
        val surveyId =savedStateHandle.get<String>(SURVEY_ID)
        if (!surveyId.isNullOrEmpty()) {
            viewModelScope.launch {
                if(!responseId.isNullOrEmpty()) {
                    response.value = surveysRepository.getResponse(responseId) ?: SurveyResponse()
                    answers.value= createAnswerMap(response.value.answers)
                }
                survey.value =
                    surveysRepository.getSurvey(surveyId) ?: Survey()
                responsesTest = surveysRepository.getResponsesWithAnswersTest(surveyId)
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