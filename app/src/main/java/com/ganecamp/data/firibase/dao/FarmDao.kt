package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.model.Farm
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.data.firibase.FirestoreErrorEvaluator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FarmDao @Inject constructor(db: FirebaseFirestore) {

    private val farmsCollection = db.collection(FirestoreCollections.FARM_COLLECTION)

    suspend fun getFarmByToken(token: String): Pair<Farm?, RepositoryRespond> {
        return try {
            val querySnapshot = farmsCollection.whereEqualTo("token", token).get().await()
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val farm = document.toObject<Farm>()
                farm?.let {
                    it.id = document.id
                    return Pair(farm, RepositoryRespond.OK)
                }
            }
            Pair(null, RepositoryRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getFarmByToken: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun updateFarm(farm: Farm): RepositoryRespond {
        return try {
            farm.id?.let {
                farmsCollection.document(it).set(farm).await()
                return RepositoryRespond.OK
            }
            RepositoryRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateFarm: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}