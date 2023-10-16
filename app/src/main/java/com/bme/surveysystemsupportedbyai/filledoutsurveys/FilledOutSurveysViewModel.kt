package com.bme.surveysystemsupportedbyai.filledoutsurveys

import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FilledOutSurveysViewModel @Inject constructor(
    private val repo: AuthRepository,
    surveysRepository: SurveysRepository
): ViewModel() {
    val filledOutSurveys = surveysRepository.filledOutSurveys
    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false

    fun signOut() = repo.signOut()

}