package com.ganecamp.data.firestore

import com.ganecamp.data.error.DataError
import com.ganecamp.data.firestore.util.FirestoreCollections
import com.ganecamp.data.model.FirestoreFarm
import com.ganecamp.data.result.DataResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FarmDataSource @Inject constructor(db: FirebaseFirestore) {

    private val farmsCollection = db.collection(FirestoreCollections.FARM_COLLECTION)

    suspend fun getFarmByToken(token: String): DataResult<FirestoreFarm> {
        return try {
            val querySnapshot = farmsCollection.whereEqualTo("token", token).get().await()
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val farm = document.toObject<FirestoreFarm>()

                farm?.let {
                    farm.id = document.id
                    return DataResult.Success(farm)
                }
            }
            DataResult.Error(DataError.NotFoundError())
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun updateFarm(farm: FirestoreFarm): DataResult<Unit> {
        return try {
            farmsCollection.document(farm.id!!).set(farm).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}