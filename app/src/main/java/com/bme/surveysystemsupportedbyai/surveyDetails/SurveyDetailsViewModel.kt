package com.bme.surveysystemsupportedbyai.surveyDetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.SurveyRaw
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository
) : ViewModel() {
    val survey = mutableStateOf(SurveyRaw())

    init {
        val surveyId =savedStateHandle.get<String>(SURVEY_ID) //"GKRvrEUl5u80XHJNMbqT"
        if (surveyId != null) {
            viewModelScope.launch {
                survey.value =
                    surveysRepository.getSurvey(surveyId.idFromParameter()) ?: SurveyRaw()
            }
        }
    }

    fun onOptionSelected(option:String){

    }
}
fun String.idFromParameter(): String {
    return this.substring(1, this.length - 1)
}