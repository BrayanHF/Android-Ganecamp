package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.model.Farm
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.functions.FirestoreErrorEvaluator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FarmDao @Inject constructor(db: FirebaseFirestore) {

    private val farmsCollection = db.collection(FirestoreCollections.FARM_COLLECTION)

    suspend fun getFarmByToken(token: String): Pair<Farm?, FirestoreRespond> {
        return try {
            val querySnapshot = farmsCollection.whereEqualTo("token", token).get().await()
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val farm = document.toObject<Farm>()
                farm?.let {
                    it.id = document.id
                    return Pair(farm, FirestoreRespond.OK)
                }
            }
            Pair(null, FirestoreRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getFarmByToken: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun updateFarm(farm: Farm): FirestoreRespond {
        return try {
            farm.id?.let {
                farmsCollection.document(it).set(farm).await()
                return FirestoreRespond.OK
            }
            FirestoreRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateFarm: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}