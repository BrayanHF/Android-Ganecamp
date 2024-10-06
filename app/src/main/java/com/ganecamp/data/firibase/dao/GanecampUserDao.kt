package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.model.objects.GanecampUser
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.enums.FirestoreRespond.OK
import com.ganecamp.utilities.functions.FirestoreErrorEvaluator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GanecampUserDao @Inject constructor(db: FirebaseFirestore) {

    private val usersCollection = db.collection(FirestoreCollections.USER_COLLECTION)

    suspend fun getUserByEmail(email: String): Pair<GanecampUser?, FirestoreRespond> {
        return try {
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val user = document.toObject<GanecampUser>()
                user?.id = document.id
                return Pair(user, OK)
            }
            Pair(null, FirestoreRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getUserByEmail: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun createUser(user: GanecampUser): FirestoreRespond {
        return try {
            user.email.let {
                usersCollection.document().set(user).await()
                return OK
            }
            FirestoreRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in createUser: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    // Todo: Update email in firebase auth
    suspend fun updateUser(user: GanecampUser): FirestoreRespond {
        return try {
            user.email.let {
                usersCollection.document(it).set(user).await()
                return OK
            }
            FirestoreRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateUser: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}