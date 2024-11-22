package com.ganecamp.data.firestore

import com.ganecamp.data.error.DataError
import com.ganecamp.domain.session.FarmSessionManager
import com.ganecamp.data.firestore.util.FirestoreCollections
import com.ganecamp.data.firestore.util.getSourceFrom
import com.ganecamp.data.model.FirestoreAnimalVaccine
import com.ganecamp.data.model.FirestoreVaccineApplied
import com.ganecamp.data.network.NetworkStatusHelper
import com.ganecamp.data.result.DataResult
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccineAppliedDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val vaccineDao: VaccineDataSource,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getAnimalVaccinesCollectionReference(animalId: String): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        return farm?.id?.let {
            db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.ANIMAL_COLLECTION).document(animalId)
                .collection(FirestoreCollections.ANIMAL_VACCINE_COLLECTION)
        }
    }

    suspend fun getVaccinesApplied(animalId: String): DataResult<List<FirestoreVaccineApplied>> {
        val animalVaccinesReference =
            getAnimalVaccinesCollectionReference(animalId) ?: return DataResult.Error(
                DataError.NoFarmSessionError()
            )

        return try {
            val animalVaccines = mutableListOf<FirestoreVaccineApplied>()
            val animalVaccinesCollection =
                animalVaccinesReference.get(networkStatusHelper.getSourceFrom()).await()
            animalVaccinesCollection.forEach { document ->
                val animalVaccine = document.toObject<FirestoreAnimalVaccine>()
                when (val vaccineRespond = vaccineDao.getVaccineById(animalVaccine.vaccineId)) {
                    is DataResult.Success -> {
                        val vaccineApplied = FirestoreVaccineApplied(
                            id = document.id,
                            vaccineId = animalVaccine.vaccineId,
                            date = animalVaccine.date,
                            name = vaccineRespond.data.name,
                            description = vaccineRespond.data.description
                        )
                        animalVaccines.add(vaccineApplied)
                    }

                    is DataResult.Error -> return DataResult.Error(vaccineRespond.exception)
                }
            }
            DataResult.Success(animalVaccines)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun applyVaccineAnimal(
        animalId: String, animalVaccine: FirestoreAnimalVaccine
    ): DataResult<Unit> {
        val animalVaccinesReference = getAnimalVaccinesCollectionReference(animalId)
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            animalVaccinesReference.add(animalVaccine).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun updateVaccineApplied(
        animalId: String, animalVaccine: FirestoreAnimalVaccine
    ): DataResult<Unit> {
        val animalVaccinesReference = getAnimalVaccinesCollectionReference(animalId)
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            animalVaccinesReference.document(animalVaccine.id!!).set(animalVaccine).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun deleteVaccineApplied(id: String): DataResult<Unit> {
        val animalVaccinesReference = getAnimalVaccinesCollectionReference(id)
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            animalVaccinesReference.document(id).delete().await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}