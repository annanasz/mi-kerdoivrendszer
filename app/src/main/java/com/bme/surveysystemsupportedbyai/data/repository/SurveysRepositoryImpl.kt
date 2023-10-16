package com.bme.surveysystemsupportedbyai.data.repository

import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey
import com.bme.surveysystemsupportedbyai.domain.model.SentSurvey
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query


@Singleton
class SurveysRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository
) : SurveysRepository {
    override val userSurveys: Flow<List<Survey>>
        get() = firestore.collection(SURVEYS_COLLECTION)
            .whereEqualTo(CREATOR_ID_FIELD, auth.currentUser?.uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .dataObjects<Survey>()

    override suspend fun getSurvey(surveyId: String): Survey? {
        val surveyDoc = firestore.collection(SURVEYS_COLLECTION).document(surveyId).get().await()
        val survey = surveyDoc.toObject<Survey>()

        if (survey != null) {
            val questionsDoc = surveyDoc.reference
                .collection(QUESTIONS_COLLECTION)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()

            val questions = questionsDoc.documents.map { it.toObject<Question>() }
            return survey.copy(questions = questions as List<Question>)
        }
        return null
    }

    override suspend fun updateSurvey(survey: Survey) {
        val surveyRef = firestore.collection(SURVEYS_COLLECTION).document(survey.id)
        val questionsCollectionRef = surveyRef.collection(QUESTIONS_COLLECTION)
        for (question in survey.questions) {
            if (question.id != "") {
                val questionRef = surveyRef.collection(QUESTIONS_COLLECTION).document(question.id)
                questionRef.update("text", question.text, "options",question.options)
                    .await()
            }
            else{
                val newQuestionRef = questionsCollectionRef.document()
                val newQuestion = question.copy(id = newQuestionRef.id, timestamp = Timestamp.now())
                newQuestionRef.set(newQuestion).await()

                question.id = newQuestionRef.id
            }
        }
        firestore.collection(SURVEYS_COLLECTION).document(survey.id).set(survey).await()

        val existingQuestionsSnapshot = questionsCollectionRef.get().await()
        for (document in existingQuestionsSnapshot.documents) {
            val questionId = document.id
            if (survey.questions?.none { it.id == questionId } ?: false) {
                questionsCollectionRef.document(questionId).delete().await()
            }
        }
    }


    override suspend fun deleteSurvey(surveyId: String) {
        firestore.collection(SURVEYS_COLLECTION).document(surveyId).delete().await()
        val questionsCollection = firestore.collection(SURVEYS_COLLECTION)
            .document(surveyId)
            .collection(QUESTIONS_COLLECTION)

        val questionsQuerySnapshot = questionsCollection.get().await()

        for (document in questionsQuerySnapshot.documents) {
            document.reference.delete()
        }
    }

    override suspend fun saveSentSurvey(sentSurvey: SentSurvey) {
        val newSentSurveyRef = firestore.collection(SENT_SURVEYS_COLLECTION).document()
        val newSentSurvey = sentSurvey.copy(id = newSentSurveyRef.id, timestamp = Timestamp.now())
        newSentSurveyRef.set(newSentSurvey).await()
        sentSurvey.id = newSentSurvey.id
        editSurveyOnSend(sentSurvey.surveyId)
    }

    override val sentSurveys: Flow<List<SentSurvey>>
        get() = firestore.collection(SENT_SURVEYS_COLLECTION)
            .whereEqualTo(SENDER_ID_FIELD,auth.currentUser?.uid)
            .orderBy("timestamp",Query.Direction.DESCENDING)
            .dataObjects<SentSurvey>()

    override suspend fun saveReceivedSurvey(receivedSurveys: List<ReceivedSurvey>) {
        for (receivedSurvey in receivedSurveys) {
            val newReceivedSurveyRef = firestore.collection(RECEIVED_SURVEYS_COLLECTION).document()
            val newReceivedSurvey = receivedSurvey.copy(id = newReceivedSurveyRef.id, timestamp = Timestamp.now())
            newReceivedSurveyRef.set(newReceivedSurvey).await()
            receivedSurvey.id = newReceivedSurvey.id
        }
    }

    override suspend fun fillOutSurvey(surveyResponse: SurveyResponse):Boolean {
        val surveyResponseWithId = surveyResponse.copy(userId = auth.currentUser!!.uid, timestamp = Timestamp.now(), userEmail = auth.currentUser!!.email!!)
        val surveyResponseRef = firestore.collection(RESPONSES_COLLECTION).document()
        val surveyResponseId = surveyResponseRef.id
        val responseToSave = saveFillOutSurveyAnswers(surveyResponseWithId, responseId = surveyResponseId)
        surveyResponseRef.set(responseToSave).await()
        return true
    }

    override val receivedSurveys: Flow<List<ReceivedSurvey>>
        get() = firestore.collection(RECEIVED_SURVEYS_COLLECTION)
            .whereEqualTo(RECIPIENT_EMAIL_FIELD, auth.currentUser?.email)
            .whereEqualTo(FILLED_FIELD, false)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .dataObjects()
    override val filledOutSurveys: Flow<List<SurveyResponse>>
        get() = firestore.collection(RESPONSES_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, auth.currentUser?.uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .dataObjects()

    suspend fun editSurveyOnSend(id: String){
        val originalSurveyDocRef = firestore.collection(SURVEYS_COLLECTION).document(id)
        val originalSurveyDoc = originalSurveyDocRef.get().await()
        val originalSurveyData = originalSurveyDoc.toObject<Survey>()

        if(originalSurveyData!=null){
            val newSurveyRef = firestore.collection(SURVEYS_COLLECTION).document()
            val newSurveyId = newSurveyRef.id
            val copiedSurveyData = copySurveyWithQuestions(originalSurveyData, newSurveyId)
            newSurveyRef.set(copiedSurveyData).await()
            originalSurveyDocRef.update("creatorId", "none").await()
        }
    }
    override suspend fun saveSurvey(survey: Survey):String {
        val surveyWithUserId = survey.copy(creatorId = auth.currentUser!!.uid, timestamp = Timestamp.now())
        val surveyRef = firestore.collection(SURVEYS_COLLECTION).document()
        val surveyId = surveyRef.id
        val copiedSurveyData = copySurveyWithQuestions(surveyWithUserId, surveyId, true)
        surveyRef.set(copiedSurveyData).await()
        return surveyId
    }
    private suspend fun copySurveyWithQuestions(originalSurvey: Survey, newSurveyId: String, timestampNeeded:Boolean=false): Survey {
        val copiedSurveyData = originalSurvey.copy(id = newSurveyId)
        val copiedQuestions = originalSurvey.questions.map { originalQuestion ->
            val newQuestionRef = firestore.collection(SURVEYS_COLLECTION).document(newSurveyId)
                .collection(QUESTIONS_COLLECTION).document()
            val newQuestionId = newQuestionRef.id
            val newQuestionData = when(timestampNeeded){
                true ->  originalQuestion.copy(id = newQuestionId, timestamp = Timestamp.now())
                else ->  originalQuestion.copy(id = newQuestionId)
            }
            newQuestionRef.set(newQuestionData).await()
            newQuestionData
        }

        return copiedSurveyData.copy(questions = copiedQuestions)
    }

    private suspend fun saveFillOutSurveyAnswers(originalSurveyResponse: SurveyResponse, responseId:String):SurveyResponse{
        val newAnswers = originalSurveyResponse.answers.map{answer->
            val newAnswerRef = firestore.collection(RESPONSES_COLLECTION).document(responseId).collection(
                ANSWER_COLLECTION).document()
            val newAnswerId = newAnswerRef.id
            val newAnswerData = answer.copy(id = newAnswerId)
            newAnswerRef.set(newAnswerData).await()
            newAnswerData
        }
        return originalSurveyResponse.copy(id = responseId, answers = newAnswers)
    }


    companion object {
        private const val CREATOR_ID_FIELD = "creatorId"
        private const val SURVEYS_COLLECTION = "surveys"
        private const val QUESTIONS_COLLECTION = "questions"
        private const val SENT_SURVEYS_COLLECTION = "sent_surveys"
        private const val RECEIVED_SURVEYS_COLLECTION = "received_surveys"
        private const val RECIPIENT_EMAIL_FIELD = "recipientEmail"
        private const val FILLED_FIELD = "filled"
        private const val SENDER_ID_FIELD = "senderId"
        private const val RESPONSES_COLLECTION = "responses"
        private const val ANSWER_COLLECTION = "answers"
        private const val USER_ID_FIELD = "userId"
    }
}
