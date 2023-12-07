package com.bme.surveysystemsupportedbyai.ui.sentsurveys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.SentSurvey
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentSurveysViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val surveysRepository: SurveysRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SentSurveysUiState())
    val uiState: StateFlow<SentSurveysUiState> = _uiState.asStateFlow()

    fun fetchData() {
        viewModelScope.launch {
            try {
                surveysRepository.sentSurveys.collect { surveys ->
                    _uiState.value = _uiState.value.copy(sentSurveys = surveys)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }
    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false

    fun signOut() = repo.signOut()

}

data class SentSurveysUiState(
    val sentSurveys: List<SentSurvey> = emptyList(),
    val errorMessage: String? = null
)