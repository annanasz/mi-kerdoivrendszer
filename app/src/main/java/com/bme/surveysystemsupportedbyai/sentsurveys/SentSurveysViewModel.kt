package com.bme.surveysystemsupportedbyai.sentsurveys

import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SentSurveysViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val surveysRepository: SurveysRepository
): ViewModel() {
    val sentSurveys = surveysRepository.sentSurveys
    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false

    fun signOut() = repo.signOut()

}