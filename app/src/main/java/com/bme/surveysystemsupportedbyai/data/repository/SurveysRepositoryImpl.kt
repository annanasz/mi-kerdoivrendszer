package com.bme.surveysystemsupportedbyai.data.repository

import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SurveysRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository
) : SurveysRepository {

    fun getSurveys(): List<Survey> {
        TODO("Not yet implemented")
    }

    override val userSurveys: Flow<List<Survey>>
        get() = firestore.collection("surveys").whereEqualTo("userId", auth.currentUser?.uid)
            .dataObjects<Survey>()

    override suspend fun getSurvey(surveyId: String): Survey? {
        TODO("Not yet implemented")
    }
}