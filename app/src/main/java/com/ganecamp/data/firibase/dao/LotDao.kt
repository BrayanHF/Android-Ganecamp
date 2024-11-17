package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FarmSessionManager
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.getSourceFrom
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.domain.network.NetworkStatusHelper
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.functions.FirestoreErrorEvaluator
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LotDao @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getLotCollectionReference(): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        farm?.id?.let {
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.LOT_COLLECTION)
        }
        return null
    }

    suspend fun getAllLots(): Pair<List<Lot>, FirestoreRespond> {
        val lotCollectionReference = getLotCollectionReference() ?: return Pair(
            emptyList(), FirestoreRespond.NO_FARM_SESSION
        )

        return try {
            val lots = mutableListOf<Lot>()
            val lotsCollection =
                lotCollectionReference.get(networkStatusHelper.getSourceFrom()).await()
            lotsCollection.forEach { document ->
                val lot = document.toObject<Lot>()
                lot.id = document.id
                lots.add(lot)
            }
            Pair(lots, FirestoreRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getAllLots: ${e.message}")
            Pair(emptyList(), FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun getAnimalsByLotId(lotId: String): Pair<List<Animal>, FirestoreRespond> {
        val farmId = farmSessionManager.getFarm()?.id ?: return Pair(
            emptyList(), FirestoreRespond.NO_FARM_SESSION
        )

        return try {
            val animals = mutableListOf<Animal>()
            val animalCollectionReference =
                db.collection(FirestoreCollections.FARM_COLLECTION).document(farmId)
                    .collection(FirestoreCollections.ANIMAL_COLLECTION)

            val querySnapshot = animalCollectionReference.whereEqualTo("lotId", lotId)
                .get(networkStatusHelper.getSourceFrom()).await()
            querySnapshot?.documents?.forEach { document ->
                val animal = document.toObject<Animal>()
                animal?.let {
                    it.id = document.id
                    animals.add(it)
                }
            }
            Pair(animals, FirestoreRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getAnimalsByLotId: ${e.message}")
            Pair(emptyList(), FirestoreErrorEvaluator.evaluateError(e))
        }
    }


    suspend fun getLotById(id: String): Pair<Lot?, FirestoreRespond> {

        val lotCollectionReference =
            getLotCollectionReference() ?: return Pair(null, FirestoreRespond.NO_FARM_SESSION)

        return try {
            val documentSnapshot =
                lotCollectionReference.document(id).get(networkStatusHelper.getSourceFrom()).await()
            val lot = documentSnapshot.toObject<Lot>()
            lot?.let {
                lot.id = documentSnapshot.id
                return Pair(lot, FirestoreRespond.OK)
            }
            Pair(null, FirestoreRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getLotById: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun createLot(lot: Lot): Pair<String?, FirestoreRespond> {
        val lotCollectionReference =
            getLotCollectionReference() ?: return Pair(null, FirestoreRespond.NO_FARM_SESSION)
        return try {
            val documentReference = lotCollectionReference.add(lot).await()
            Pair(documentReference.id, FirestoreRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in createLot: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun updateLot(lot: Lot): FirestoreRespond {
        val lotCollectionReference =
            getLotCollectionReference() ?: return FirestoreRespond.NO_FARM_SESSION
        return try {
            lot.id?.let {
                lotCollectionReference.document(it).set(lot).await()
                return FirestoreRespond.OK
            }
            FirestoreRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateLot: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }

    }

    suspend fun deleteLotById(id: String): FirestoreRespond {
        val lotCollectionReference =
            getLotCollectionReference() ?: return FirestoreRespond.NO_FARM_SESSION
        return try {
            lotCollectionReference.document(id).delete().await()
            FirestoreRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in deleteLotById: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}