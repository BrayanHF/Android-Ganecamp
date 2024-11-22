package com.ganecamp.data.firestore

import com.ganecamp.data.error.DataError
import com.ganecamp.domain.session.FarmSessionManager
import com.ganecamp.data.firestore.util.FirestoreCollections
import com.ganecamp.data.firestore.util.getSourceFrom
import com.ganecamp.data.model.FirestoreEvent
import com.ganecamp.data.network.NetworkStatusHelper
import com.ganecamp.data.result.DataResult
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val networkStatusHelper: NetworkStatusHelper
) {

    //Todo: Independents general events
    private val generalEventCollectionReference =
        db.collection(FirestoreCollections.GENERAL_EVENT_COLLECTION)

    private fun getEventCollectionReference(): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        return farm?.id?.let {
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.FARM_EVENT_COLLECTION)
        }
    }

    suspend fun getAllFarmEvents(): DataResult<List<FirestoreEvent>> {
        val eventsReference =
            getEventCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val events = mutableListOf<FirestoreEvent>()
            val farmEventCollection =
                eventsReference.get(networkStatusHelper.getSourceFrom()).await()
            farmEventCollection.forEach { document ->
                val event = document.toObject<FirestoreEvent>()
                event.id = document.id
                events.add(event)
            }
            DataResult.Success(events)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getFarmEventById(id: String): DataResult<FirestoreEvent> {
        val eventsReference =
            getEventCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val farmEventDocument =
                eventsReference.document(id).get(networkStatusHelper.getSourceFrom()).await()

            val event = farmEventDocument.toObject<FirestoreEvent>()
            event?.let {
                event.id = farmEventDocument.id
                DataResult.Success(it)
            } ?: DataResult.Error(DataError.NotFoundError())
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun addFarmEvent(event: FirestoreEvent): DataResult<String> {
        val eventsReference =
            getEventCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val documentReference = eventsReference.add(event).await()
            DataResult.Success(documentReference.id)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun updateFarmEvent(event: FirestoreEvent): DataResult<Unit> {
        val eventsReference =
            getEventCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            eventsReference.document(event.id!!).set(event).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun deleteFarmEventById(id: String): DataResult<Unit> {
        val eventsReference =
            getEventCollectionReference() ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            eventsReference.document(id).delete().await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}