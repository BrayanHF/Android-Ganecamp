package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FarmSessionManager
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.getSourceFrom
import com.ganecamp.data.firibase.model.Event
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
class EventDao @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private val generalEventCollectionReference =
        db.collection(FirestoreCollections.GENERAL_EVENT_COLLECTION)

    private fun getEventCollectionReference(): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        farm?.id?.let {
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.FARM_EVENT_COLLECTION)
        }
        return null
    }

    suspend fun getAllEvents(): Pair<List<Event>, RepositoryRespond> {
        val eventCollectionReference = getEventCollectionReference() ?: return Pair(
            emptyList(), RepositoryRespond.NO_FARM_SESSION
        )

        return try {
            val events = mutableListOf<Event>()
            val farmEventCollection =
                eventCollectionReference.get(networkStatusHelper.getSourceFrom()).await()
            farmEventCollection.forEach { document ->
                val event = document.toObject<Event>()
                event.id = document.id
                events.add(event)
            }

            val generalEventCollection =
                generalEventCollectionReference.get(networkStatusHelper.getSourceFrom()).await()
            generalEventCollection.forEach { document ->
                val event = document.toObject<Event>()
                event.id = document.id
                events.add(event)
            }
            Pair(events, RepositoryRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getAllEvents: ${e.message}")
            Pair(emptyList(), FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun getEventById(id: String): Pair<Event?, RepositoryRespond> {
        val eventCollectionReference =
            getEventCollectionReference() ?: return Pair(null, RepositoryRespond.NO_FARM_SESSION)

        return try {
            val event: Event?
            val farmEventDocument =
                eventCollectionReference.document(id).get(networkStatusHelper.getSourceFrom())
                    .await()
            if (farmEventDocument != null) {
                event = farmEventDocument.toObject<Event>()
                event?.let {
                    event.id = farmEventDocument.id
                    return Pair(event, RepositoryRespond.OK)
                }
            } else {
                val generalEventDocument = generalEventCollectionReference.document(id)
                    .get(networkStatusHelper.getSourceFrom()).await()
                event = generalEventDocument.toObject<Event>()
                event?.let {
                    event.id = generalEventDocument.id
                    return Pair(event, RepositoryRespond.OK)
                }
            }
            Pair(null, RepositoryRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getEventById: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun createEvent(event: Event): Pair<String?, RepositoryRespond> {
        val eventCollectionReference =
            getEventCollectionReference() ?: return Pair(null, RepositoryRespond.NO_FARM_SESSION)

        return try {
            val documentReference = eventCollectionReference.add(event).await()
            Pair(documentReference.id, RepositoryRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in createEvent: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun updateEvent(event: Event): RepositoryRespond {
        val eventCollectionReference =
            getEventCollectionReference() ?: return RepositoryRespond.NO_FARM_SESSION

        return try {
            event.id?.let {
                eventCollectionReference.document(it).set(event).await()
                RepositoryRespond.OK
            }
            RepositoryRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateEvent: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun deleteEventById(id: String): RepositoryRespond {
        val eventCollectionReference =
            getEventCollectionReference() ?: return RepositoryRespond.NO_FARM_SESSION

        return try {
            eventCollectionReference.document(id).delete().await()
            RepositoryRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in deleteEventById: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}