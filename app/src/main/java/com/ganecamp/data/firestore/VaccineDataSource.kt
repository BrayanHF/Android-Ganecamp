package com.ganecamp.data.firestore

import com.ganecamp.data.error.DataError
import com.ganecamp.data.firestore.util.FirestoreCollections
import com.ganecamp.data.firestore.util.getSourceFrom
import com.ganecamp.data.model.FirestoreVaccine
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
class VaccineDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getFarmVaccineCollectionReference(): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        return farm?.id?.let {
            db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.FARM_VACCINE_COLLECTION)
        }
    }

    suspend fun getAllVaccines(): DataResult<List<FirestoreVaccine>> {
        val vaccinesReference = getFarmVaccineCollectionReference() ?: return DataResult.Error(
            DataError.NoFarmSessionError()
        )

        return try {
            val vaccines = mutableListOf<FirestoreVaccine>()
            val farmVaccineCollection =
                vaccinesReference.get(networkStatusHelper.getSourceFrom()).await()

            farmVaccineCollection.forEach { document ->
                val vaccine = document.toObject<FirestoreVaccine>()
                vaccine.id = document.id
                vaccines.add(vaccine)
            }
            DataResult.Success(vaccines)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getVaccineById(id: String): DataResult<FirestoreVaccine> {
        val vaccinesReference = getFarmVaccineCollectionReference() ?: return DataResult.Error(
            DataError.NoFarmSessionError()
        )
        return try {
            val vaccineDocument =
                vaccinesReference.document(id).get(networkStatusHelper.getSourceFrom()).await()

            val vaccine = vaccineDocument.toObject<FirestoreVaccine>()
            vaccine?.let {
                vaccine.id = vaccineDocument.id
                DataResult.Success(vaccine)
            } ?: DataResult.Error(DataError.NotFoundError())
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun addVaccine(vaccine: FirestoreVaccine): DataResult<String> {
        val vaccinesReference = getFarmVaccineCollectionReference() ?: return DataResult.Error(
            DataError.NoFarmSessionError()
        )

        return try {
            val documentReference = vaccinesReference.add(vaccine).await()
            DataResult.Success(documentReference.id)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun updateVaccine(vaccine: FirestoreVaccine): DataResult<Unit> {
        val vaccinesReference = getFarmVaccineCollectionReference() ?: return DataResult.Error(
            DataError.NoFarmSessionError()
        )
        return try {
            vaccinesReference.document(vaccine.id!!).set(vaccine).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun deleteVaccineById(id: String): DataResult<Unit> {
        val vaccinesReference = getFarmVaccineCollectionReference() ?: return DataResult.Error(
            DataError.NoFarmSessionError()
        )
        return try {
            vaccinesReference.document(id).delete().await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}