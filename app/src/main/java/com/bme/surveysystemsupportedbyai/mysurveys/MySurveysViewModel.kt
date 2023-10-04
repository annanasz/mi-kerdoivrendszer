package com.bme.surveysystemsupportedbyai.mysurveys

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.User
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class MySurveysViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val surveysRepository: SurveysRepository
): ViewModel() {

    val surveys = surveysRepository.userSurveys
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formattedDateTime = formatter.format(calendar.time)
    val surveys_dummy: List<Survey> = listOf(
        Survey(creator = User(email="valami", surveys = listOf()), questions = listOf(), responses = listOf(), timestamp = "2022.10.02", title = "Teszt") ,
        Survey(creator = User(email="valami", surveys = listOf()), questions = listOf(), responses = listOf(), timestamp = "2022.10.02", title = "Teszt3")

    )
    val isEmailVerified get() = authRepository.currentUser?.isEmailVerified ?: false

    fun signOut() = authRepository.signOut()
    fun onEditClick(survey: Survey,openScreen: (String) -> Unit) {
        openScreen(Screen.SurveyEditScreen.route)
    }

    fun onDeleteClick(survey: Survey) {

    }

    fun onSendClick(survey: Survey) {

    }

    fun onItemClick(survey: Survey, openScreen: (String) -> Unit){
        openScreen(Screen.SurveyDetailsScreen.route)
    }

}