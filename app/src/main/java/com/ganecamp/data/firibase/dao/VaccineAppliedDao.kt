package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FarmSessionManager
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.model.objects.AnimalVaccine
import com.ganecamp.model.objects.VaccineApplied
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.functions.FirestoreErrorEvaluator
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccineAppliedDao @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val vaccineDao: VaccineDao
) {

    private fun getAnimalVaccinesCollectionReference(animalId: String): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        farm?.id?.let {
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.ANIMAL_COLLECTION).document(animalId)
                .collection(FirestoreCollections.ANIMAL_VACCINE_COLLECTION)
        }
        return null
    }

    suspend fun getVaccinesApplied(animalId: String): Pair<List<VaccineApplied>, FirestoreRespond> {
        val animalVaccinesReference = getAnimalVaccinesCollectionReference(animalId) ?: return Pair(
            emptyList(), FirestoreRespond.NO_FARM_SESSION
        )

        return try {
            val animalVaccines = mutableListOf<VaccineApplied>()
            val animalVaccinesCollection = animalVaccinesReference.get().await()
            animalVaccinesCollection.forEach { document ->
                val animalVaccine = document.toObject<AnimalVaccine>()
                val vaccineRespond = vaccineDao.getVaccineById(animalVaccine.vaccineId)
                if (vaccineRespond.second == FirestoreRespond.OK) {
                    val vaccineApplied = VaccineApplied(
                        id = document.id,
                        vaccineId = animalVaccine.vaccineId,
                        date = animalVaccine.date,
                        name = vaccineRespond.first?.name ?: "",
                        description = vaccineRespond.first?.description ?: ""
                    )
                    animalVaccines.add(vaccineApplied)
                }
            }
            Pair(animalVaccines, FirestoreRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getVaccinesApplied: ${e.message}")
            Pair(emptyList(), FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun applyVaccineAnimal(
        animalId: String, animalVaccine: AnimalVaccine
    ): FirestoreRespond {
        val animalVaccinesReference = getAnimalVaccinesCollectionReference(animalId)
            ?: return FirestoreRespond.NO_FARM_SESSION

        return try {
            animalVaccinesReference.add(animalVaccine).await()
            FirestoreRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in applyVaccineAnimal: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun updateVaccineApplied(
        animalId: String, animalVaccine: AnimalVaccine
    ): FirestoreRespond {
        val animalVaccinesReference = getAnimalVaccinesCollectionReference(animalId)
            ?: return FirestoreRespond.NO_FARM_SESSION

        return try {
            animalVaccine.id?.let {
                animalVaccinesReference.document(it).set(animalVaccine).await()
                return FirestoreRespond.OK
            }
            FirestoreRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateVaccineApplied: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun deleteVaccineApplied(id: String): FirestoreRespond {
        val animalVaccinesReference =
            getAnimalVaccinesCollectionReference(id) ?: return FirestoreRespond.NO_FARM_SESSION

        return try {
            animalVaccinesReference.document(id).delete().await()
            FirestoreRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in deleteVaccineApplied: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}