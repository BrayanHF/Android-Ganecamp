package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.model.GanecampUser
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.domain.enums.RepositoryRespond.OK
import com.ganecamp.data.firibase.FirestoreErrorEvaluator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GanecampUserDao @Inject constructor(db: FirebaseFirestore) {

    private val usersCollection = db.collection(FirestoreCollections.USER_COLLECTION)

    suspend fun getUserByEmail(email: String): Pair<GanecampUser?, RepositoryRespond> {
        return try {
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
            if (querySnapshot.isEmpty) return Pair(null, RepositoryRespond.NOT_FOUND)
            val document = querySnapshot.documents.first()
            val user = document.toObject<GanecampUser>()
            user?.id = document.id
            Pair(user, OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getUserByEmail: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun createUser(user: GanecampUser): RepositoryRespond {
        return try {
            usersCollection.add(user).await()
            return OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in createUser: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    // Todo: Update email in firebase auth
    suspend fun updateUser(user: GanecampUser): RepositoryRespond {
        return try {
            user.id?.let {
                usersCollection.document(it).set(user).await()
                return OK
            }
            RepositoryRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateUser: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}