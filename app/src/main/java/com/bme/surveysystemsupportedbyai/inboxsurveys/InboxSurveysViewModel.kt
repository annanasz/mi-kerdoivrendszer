package com.bme.surveysystemsupportedbyai.inboxsurveys

import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InboxSurveysViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false

    fun signOut() = repo.signOut()

}