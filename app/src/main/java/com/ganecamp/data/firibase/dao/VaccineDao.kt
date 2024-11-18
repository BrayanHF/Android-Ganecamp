package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FarmSessionManager
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.getSourceFrom
import com.ganecamp.data.firibase.model.Vaccine
import com.ganecamp.domain.network.NetworkStatusHelper
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.data.firibase.FirestoreErrorEvaluator
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccineDao @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getFarmVaccineCollectionReference(): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        farm?.id?.let {
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.FARM_VACCINE_COLLECTION)
        }
        return null
    }

    suspend fun getAllVaccines(): Pair<List<Vaccine>, RepositoryRespond> {
        val farmVaccineCollectionReference = getFarmVaccineCollectionReference() ?: return Pair(
            emptyList(), RepositoryRespond.NO_FARM_SESSION
        )

        return try {
            val vaccines = mutableListOf<Vaccine>()
            val farmVaccineCollection =
                farmVaccineCollectionReference.get(networkStatusHelper.getSourceFrom()).await()
            farmVaccineCollection.forEach { document ->
                val vaccine = document.toObject<Vaccine>()
                vaccine.id = document.id
                vaccines.add(vaccine)
            }
            Pair(vaccines, RepositoryRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getAllVaccines: ${e.message}")
            Pair(emptyList(), FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun getVaccineById(id: String): Pair<Vaccine?, RepositoryRespond> {
        val farmVaccineCollectionReference = getFarmVaccineCollectionReference() ?: return Pair(
            null, RepositoryRespond.NO_FARM_SESSION
        )
        return try {
            val vaccine: Vaccine?
            val farmVaccineDocument =
                farmVaccineCollectionReference.document(id).get(networkStatusHelper.getSourceFrom())
                    .await()
            if (farmVaccineDocument != null) {
                vaccine = farmVaccineDocument.toObject<Vaccine>()
                if (vaccine != null) {
                    vaccine.id = farmVaccineDocument.id
                    return Pair(vaccine, RepositoryRespond.OK)
                }
            }
            Pair(null, RepositoryRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getVaccineById: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun createVaccine(vaccine: Vaccine): Pair<String?, RepositoryRespond> {
        val farmVaccineCollectionReference = getFarmVaccineCollectionReference() ?: return Pair(
            null,
            RepositoryRespond.NO_FARM_SESSION
        )
        return try {
            val documentReference = farmVaccineCollectionReference.add(vaccine).await()
            Pair(documentReference.id, RepositoryRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in createVaccine: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun updateVaccine(vaccine: Vaccine): RepositoryRespond {
        val farmVaccineCollectionReference =
            getFarmVaccineCollectionReference() ?: return RepositoryRespond.NO_FARM_SESSION
        return try {
            vaccine.id?.let {
                farmVaccineCollectionReference.document(it).set(vaccine).await()
                return RepositoryRespond.OK
            }
            RepositoryRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateVaccine: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun deleteVaccineById(id: String): RepositoryRespond {
        val farmVaccineCollectionReference =
            getFarmVaccineCollectionReference() ?: return RepositoryRespond.NO_FARM_SESSION
        return try {
            farmVaccineCollectionReference.document(id).delete().await()
            RepositoryRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in deleteVaccineById: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}