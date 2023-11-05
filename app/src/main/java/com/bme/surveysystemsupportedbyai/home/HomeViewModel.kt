package com.bme.surveysystemsupportedbyai.home

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    var selectedItemIndex = mutableIntStateOf(0)

    fun signOut() = repo.signOut()

    fun navigate(destination: Int){
        selectedItemIndex.intValue = destination
    }

}