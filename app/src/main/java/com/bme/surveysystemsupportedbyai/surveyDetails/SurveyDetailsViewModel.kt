package com.bme.surveysystemsupportedbyai.surveyDetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.User
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SurveyDetailsViewModel @Inject constructor(
    private val surveysRepository: SurveysRepository
) : ViewModel() {
    val survey = mutableStateOf(Survey())

    init {// majd ha megy a firestore, itt le kell keni az adott id-val rendelkezo taskot, most csak dummy adatok
        var survey_new = Survey(
            creator = User(email = "valami", surveys = listOf()), questions = listOf(
                Question(
                    type = "multiple_choice",
                    options = listOf("elso", "masodik", "harmadik", "negyedik"),
                    isRequired = true,
                    text = "ez egy proba multiple_choice"
                ),
                Question(
                    type = "multiple_choice",
                    options = listOf("elso", "masodik", "harmadik", "negyedik"),
                    isRequired = true,
                    text = "ez a masodik proba multiple_choice"
                ),
                Question(
                    type = "checkbox",
                    options = listOf("elso", "masodik", "harmadik", "negyedik"),
                    isRequired = true,
                    text = "ez egy proba checkbox"
                ),
                Question(
                    type = "checkbox",
                    options = listOf("elso", "masodik", "harmadik", "negyedik"),
                    isRequired = true,
                    text = "ez egy masik proba checkbox"
                ),
                Question(
                    type = "short_answer",
                    isRequired = true,
                    options = listOf(),
                    text = "ez egy rovid valasz"
                )
            ), responses = listOf(), timestamp = "2022.10.02", title = "Teszt3"
        )
        survey.value = survey_new
    }

    fun onOptionSelected(option:String){

    }

}