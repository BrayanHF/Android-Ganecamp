package com.ganecamp.data.firestore

import com.ganecamp.data.error.DataError
import com.ganecamp.data.firestore.util.FirestoreCollections
import com.ganecamp.data.model.FirestoreGanecampUser
import com.ganecamp.data.result.DataResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GanecampUserDataSource @Inject constructor(db: FirebaseFirestore) {

    private val usersCollection = db.collection(FirestoreCollections.USER_COLLECTION)

    suspend fun getUserByEmail(email: String): DataResult<FirestoreGanecampUser> {
        return try {
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
            if (querySnapshot.isEmpty) return DataResult.Error(DataError.NotFoundError())
            val document = querySnapshot.documents.first()
            val user = document.toObject<FirestoreGanecampUser>()
            user?.id = document.id
            DataResult.Success(user!!)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun addUser(user: FirestoreGanecampUser): DataResult<Unit> {
        return try {
            usersCollection.add(user).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    // Todo: Update email in firebase auth
    suspend fun updateUser(user: FirestoreGanecampUser): DataResult<Unit> {
        return try {
            usersCollection.document(user.id!!).set(user).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}