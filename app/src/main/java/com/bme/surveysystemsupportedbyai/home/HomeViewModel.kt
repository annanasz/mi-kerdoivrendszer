package com.bme.surveysystemsupportedbyai.home

import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false

    fun signOut() = repo.signOut()

}