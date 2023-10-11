package com.bme.surveysystemsupportedbyai.surveyfillout

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.SurveyRaw
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID
import com.bme.surveysystemsupportedbyai.surveyDetails.idFromParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyFillOutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository
): ViewModel(){
    val survey = mutableStateOf(SurveyRaw())
    val selectedOptions = mutableStateOf<Map<String,List<String>>>(emptyMap())

    init {
        val surveyId =savedStateHandle.get<String>(SURVEY_ID)
        if (surveyId != null) {
            viewModelScope.launch {
                survey.value =
                    surveysRepository.getSurvey(surveyId.idFromParameter()) ?: SurveyRaw()
            }
        }
    }
    fun updateSelectedOption(questionId: String, selectedOption: String) {
        val updatedSelectedOptions = selectedOptions.value.toMutableMap()

        val question = survey.value.questions.find { it.id == questionId }
        if (question != null && question.type == "checkbox") {
            val selectedOptionsForQuestion = updatedSelectedOptions[questionId]?.toMutableSet() ?: mutableSetOf()
            selectedOptionsForQuestion.add(selectedOption)
            updatedSelectedOptions[questionId] = selectedOptionsForQuestion.toList()
        } else {
            updatedSelectedOptions[questionId] = listOf(selectedOption)
        }
        selectedOptions.value = updatedSelectedOptions
    }
    fun removeSelectedOption(questionId: String, selectedOptionToRemove: String) {
        val updatedSelectedOptions = selectedOptions.value.toMutableMap()
        if (updatedSelectedOptions.containsKey(questionId)) {
            val selectedOptionsForQuestion = updatedSelectedOptions[questionId]?.toMutableSet() ?: mutableSetOf()
            selectedOptionsForQuestion.remove(selectedOptionToRemove)
            if(selectedOptionsForQuestion.isEmpty())
                updatedSelectedOptions.remove(questionId)
            else
                updatedSelectedOptions[questionId] = selectedOptionsForQuestion.toList()

            selectedOptions.value = updatedSelectedOptions
        }
    }

    fun allRequiredQuestionsAnswered():Boolean{
        return survey.value.questions.all { question ->
            !question.isRequired || selectedOptions.value.containsKey(question.id)
        }
    }
}