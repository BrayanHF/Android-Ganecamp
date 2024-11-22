package com.ganecamp.data.firestore

import com.ganecamp.data.error.DataError
import com.ganecamp.data.firestore.util.FirestoreCollections
import com.ganecamp.data.firestore.util.getSourceFrom
import com.ganecamp.data.model.FirestoreAnimal
import com.ganecamp.data.model.FirestoreLot
import com.ganecamp.data.network.NetworkStatusHelper
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.session.FarmSessionManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LotDataSource @Inject constructor(
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

    suspend fun getAllLots(): DataResult<List<FirestoreLot>> {
        val lotsReference =
            getLotCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val lots = mutableListOf<FirestoreLot>()
            val lotsCollection = lotsReference.get(networkStatusHelper.getSourceFrom()).await()
            lotsCollection.forEach { document ->
                val lot = document.toObject<FirestoreLot>()
                lot.id = document.id
                lots.add(lot)
            }
            DataResult.Success(lots)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getAnimalsByLotId(lotId: String): DataResult<List<FirestoreAnimal>> {
        val farmId = farmSessionManager.getFarm()?.id
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val animals = mutableListOf<FirestoreAnimal>()
            val animalCollectionReference =
                db.collection(FirestoreCollections.FARM_COLLECTION).document(farmId)
                    .collection(FirestoreCollections.ANIMAL_COLLECTION)

            val querySnapshot = animalCollectionReference.whereEqualTo("lotId", lotId)
                .get(networkStatusHelper.getSourceFrom()).await()

            querySnapshot?.documents?.forEach { document ->
                val animal = document.toObject<FirestoreAnimal>()
                animal?.let {
                    animal.id = document.id
                    animals.add(animal)
                }
            }
            DataResult.Success(animals)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getLotById(id: String): DataResult<FirestoreLot> {
        val lotsReference =
            getLotCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val documentSnapshot =
                lotsReference.document(id).get(networkStatusHelper.getSourceFrom()).await()
            val lot = documentSnapshot.toObject<FirestoreLot>()
            lot?.let {
                lot.id = documentSnapshot.id
                DataResult.Success(lot)
            } ?: DataResult.Error(DataError.NotFoundError())
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun addLot(lot: FirestoreLot): DataResult<String> {
        val lotsReference =
            getLotCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())
        return try {
            val documentReference = lotsReference.add(lot).await()
            DataResult.Success(documentReference.id)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun updateLot(lot: FirestoreLot): DataResult<Unit> {
        val lotsReference =
            getLotCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())
        return try {
            lotsReference.document(lot.id!!).set(lot).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }

    }

    suspend fun deleteLotById(id: String): DataResult<Unit> {
        val lotsReference =
            getLotCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())
        return try {
            lotsReference.document(id).delete().await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}