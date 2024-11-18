package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FarmSessionManager
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.getSourceFrom
import com.ganecamp.domain.network.NetworkStatusHelper
import com.ganecamp.data.firibase.model.Weight
import com.ganecamp.data.firibase.model.WeightValue
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.data.firibase.FirestoreErrorEvaluator
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightDao @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getAnimalWeightsCollectionReference(animalId: String): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        farm?.id?.let {
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.ANIMAL_COLLECTION).document(animalId)
                .collection(FirestoreCollections.WEIGHT_COLLECTION)
        }
        return null
    }

    suspend fun getAnimalWeights(idAnimal: String): Pair<List<Weight>, RepositoryRespond> {
        val animalWeightsReference = getAnimalWeightsCollectionReference(idAnimal) ?: return Pair(
            emptyList(), RepositoryRespond.NO_FARM_SESSION
        )

        return try {
            val weights = mutableListOf<Weight>()
            val animalWeightsCollection =
                animalWeightsReference.get(networkStatusHelper.getSourceFrom()).await()
            animalWeightsCollection.forEach { document ->
                val weight = document.toObject<Weight>()
                weight.id = document.id
                weights.add(weight)
            }
            Pair(weights, RepositoryRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getAnimalWeights: ${e.message}")
            Pair(emptyList(), FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun createWeight(animalId: String, weight: Weight): RepositoryRespond {
        val animalWeightsReference =
            getAnimalWeightsCollectionReference(animalId) ?: return RepositoryRespond.NO_FARM_SESSION

        return try {
            animalWeightsReference.add(weight).await()
            RepositoryRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in createWeight: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun updateWeight(animalId: String, weight: Weight): RepositoryRespond {
        val animalWeightsReference =
            getAnimalWeightsCollectionReference(animalId) ?: return RepositoryRespond.NO_FARM_SESSION
        return try {
            weight.id?.let {
                animalWeightsReference.document(it).set(weight).await()
                return RepositoryRespond.OK
            }
            RepositoryRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateWeight: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun deleteWeightById(id: String): RepositoryRespond {
        val animalWeightsReference =
            getAnimalWeightsCollectionReference(id) ?: return RepositoryRespond.NO_FARM_SESSION
        return try {
            animalWeightsReference.document(id).delete().await()
            RepositoryRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in deleteWeightById: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun loadWeightValue(): Pair<WeightValue?, RepositoryRespond> {
        farmSessionManager.getFarm() ?: return Pair(null, RepositoryRespond.NO_FARM_SESSION)
        return try {
            val weightValueQuery = db.collection(FirestoreCollections.WEIGHT_VALUE_COLLECTION)
                .orderBy("date", Query.Direction.DESCENDING).limit(1)
                .get(networkStatusHelper.getSourceFrom()).await()

            if (!weightValueQuery.isEmpty) {
                val weightValue = weightValueQuery.toObjects<WeightValue>().firstOrNull()
                weightValue?.let {
                    return Pair(it, RepositoryRespond.OK)
                }
            }
            Pair(null, RepositoryRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in loadWeightValue: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

}