package com.ganecamp.data.firestore

import com.ganecamp.data.error.DataError
import com.ganecamp.domain.session.FarmSessionManager
import com.ganecamp.data.firestore.util.FirestoreCollections
import com.ganecamp.data.firestore.util.getSourceFrom
import com.ganecamp.data.model.FirestoreWeight
import com.ganecamp.data.model.FirestoreWeightValue
import com.ganecamp.data.network.NetworkStatusHelper
import com.ganecamp.data.result.DataResult
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getAnimalWeightsCollectionReference(animalId: String): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        return farm?.id?.let {
            db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.ANIMAL_COLLECTION).document(animalId)
                .collection(FirestoreCollections.WEIGHT_COLLECTION)
        }
    }

    suspend fun getAnimalWeights(animalId: String): DataResult<List<FirestoreWeight>> {
        val animalWeightsReference = getAnimalWeightsCollectionReference(animalId)
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val weights = mutableListOf<FirestoreWeight>()
            val animalWeightsCollection =
                animalWeightsReference.get(networkStatusHelper.getSourceFrom()).await()
            animalWeightsCollection.forEach { document ->
                val weight = document.toObject<FirestoreWeight>()
                weight.id = document.id
                weights.add(weight)
            }
            DataResult.Success(weights)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun addWeight(animalId: String, weight: FirestoreWeight): DataResult<Unit> {
        val animalWeightsReference = getAnimalWeightsCollectionReference(animalId)
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            animalWeightsReference.add(weight).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun updateWeight(animalId: String, weight: FirestoreWeight): DataResult<Unit> {
        val animalWeightsReference = getAnimalWeightsCollectionReference(animalId)
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            animalWeightsReference.document(weight.id!!).set(weight).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun deleteWeightById(id: String): DataResult<Unit> {
        val animalWeightsReference = getAnimalWeightsCollectionReference(id)
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            animalWeightsReference.document(id).delete().await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun loadWeightValue(): DataResult<FirestoreWeightValue> {
        farmSessionManager.getFarm() ?: return DataResult.Error(DataError.NoFarmSessionError())
        return try {
            val weightValueQuery = db.collection(FirestoreCollections.WEIGHT_VALUE_COLLECTION)
                .orderBy("date", Query.Direction.DESCENDING).limit(1)
                .get(networkStatusHelper.getSourceFrom()).await()

            if (!weightValueQuery.isEmpty) {
                val weightValue = weightValueQuery.toObjects<FirestoreWeightValue>().firstOrNull()
                weightValue?.let {
                    return DataResult.Success(it)
                }
            }
            DataResult.Error(DataError.NotFoundError())
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}