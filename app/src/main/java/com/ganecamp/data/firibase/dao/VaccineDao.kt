package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FarmSessionManager
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.getSourceFrom
import com.ganecamp.domain.network.NetworkStatusHelper
import com.ganecamp.model.objects.Vaccine
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.functions.FirestoreErrorEvaluator
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

    private val generalVaccineCollectionReference =
        db.collection(FirestoreCollections.GENERAL_VACCINE_COLLECTION)

    private fun getFarmVaccineCollectionReference(): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        farm?.id?.let {
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.FARM_VACCINE_COLLECTION)
        }
        return null
    }

    suspend fun getAllVaccines(): Pair<List<Vaccine>, FirestoreRespond> {
        val farmVaccineCollectionReference = getFarmVaccineCollectionReference() ?: return Pair(
            emptyList(), FirestoreRespond.NO_FARM_SESSION
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

            val generalVaccineCollection =
                generalVaccineCollectionReference.get(networkStatusHelper.getSourceFrom()).await()
            generalVaccineCollection.forEach { document ->
                val vaccine = document.toObject<Vaccine>()
                vaccine.id = document.id
                vaccines.add(vaccine)
            }
            Pair(vaccines, FirestoreRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getAllVaccines: ${e.message}")
            Pair(emptyList(), FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun getVaccineById(id: String): Pair<Vaccine?, FirestoreRespond> {
        val farmVaccineCollectionReference = getFarmVaccineCollectionReference() ?: return Pair(
            null, FirestoreRespond.NO_FARM_SESSION
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
                    return Pair(vaccine, FirestoreRespond.OK)
                }
            } else {
                val generalVaccineDocument = generalVaccineCollectionReference.document(id)
                    .get(networkStatusHelper.getSourceFrom()).await()
                vaccine = generalVaccineDocument.toObject<Vaccine>()
                if (vaccine != null) {
                    vaccine.id = generalVaccineDocument.id
                    return Pair(vaccine, FirestoreRespond.OK)
                }
            }
            Pair(null, FirestoreRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getVaccineById: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun createVaccine(vaccine: Vaccine): FirestoreRespond {
        val farmVaccineCollectionReference =
            getFarmVaccineCollectionReference() ?: return FirestoreRespond.NO_FARM_SESSION
        return try {
            farmVaccineCollectionReference.add(vaccine).await()
            FirestoreRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in createVaccine: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun updateVaccine(vaccine: Vaccine): FirestoreRespond {
        val farmVaccineCollectionReference =
            getFarmVaccineCollectionReference() ?: return FirestoreRespond.NO_FARM_SESSION
        return try {
            vaccine.id?.let {
                farmVaccineCollectionReference.document(it).set(vaccine).await()
                return FirestoreRespond.OK
            }
            FirestoreRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateVaccine: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun deleteVaccineById(id: String): FirestoreRespond {
        val farmVaccineCollectionReference =
            getFarmVaccineCollectionReference() ?: return FirestoreRespond.NO_FARM_SESSION
        return try {
            farmVaccineCollectionReference.document(id).delete().await()
            FirestoreRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in deleteVaccineById: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}