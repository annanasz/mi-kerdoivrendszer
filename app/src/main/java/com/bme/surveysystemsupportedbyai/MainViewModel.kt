package com.bme.surveysystemsupportedbyai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    fun getAuthState() = repo.getAuthState(viewModelScope)

    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false
}