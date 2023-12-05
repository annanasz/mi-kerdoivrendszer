package com.bme.surveysystemsupportedbyai.ui.register


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.core.Utils.Companion.showMessage
import com.bme.surveysystemsupportedbyai.domain.model.Response
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.RegisterResponse
import com.bme.surveysystemsupportedbyai.domain.repository.SendEmailVerificationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var registerResponse by mutableStateOf<RegisterResponse>(Response.Success(false))
        private set
    var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(
        Response.Success(
            false
        )
    )
        private set

    fun registerWithEmailAndPassword(email: String, password: String, passwordAgain: String) = viewModelScope.launch {
        if(arePasswordsSame(password,passwordAgain)) {
            registerResponse = Response.Loading
            registerResponse = repo.firebaseSignUpWithEmailAndPassword(email, password)
        }
        else{
            registerResponse = Response.Failure(Exception("The passwords don't match!"))
        }
    }

    fun sendEmailVerification() = viewModelScope.launch {
        sendEmailVerificationResponse = Response.Loading
        sendEmailVerificationResponse = repo.sendEmailVerification()
    }

    private fun arePasswordsSame(password: String, passwordAgain: String):Boolean{
        return password==passwordAgain
    }
}