package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FarmSessionManager
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.getSourceFrom
import com.ganecamp.domain.network.NetworkStatusHelper
import com.ganecamp.model.objects.EntityEvent
import com.ganecamp.model.objects.EventApplied
import com.ganecamp.utilities.enums.EntityType
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.functions.FirestoreErrorEvaluator
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventAppliedDao @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val eventDao: EventDao,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getAnimalEventsCollectionReference(animalId: String): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        farm?.token?.let { token ->
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(token)
                .collection(FirestoreCollections.ANIMAL_COLLECTION).document(animalId)
                .collection(FirestoreCollections.ANIMAL_EVENT_COLLECTION)
        }
        return null
    }

    private fun getLotEventsCollectionReference(lotId: String): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        farm?.token?.let { token ->
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(token)
                .collection(FirestoreCollections.LOT_COLLECTION).document(lotId)
                .collection(FirestoreCollections.LOT_EVENT_COLLECTION)
        }
        return null
    }


    suspend fun getEntityEvents(
        entityId: String, entityType: EntityType
    ): Pair<List<EventApplied>, FirestoreRespond> {
        val entityEventReference = if (entityType == EntityType.Animal) {
            getAnimalEventsCollectionReference(entityId)
        } else {
            getLotEventsCollectionReference(entityId)
        } ?: return Pair(emptyList(), FirestoreRespond.NO_FARM_SESSION)

        return try {
            val entityEvents = mutableListOf<EventApplied>()
            val entityEventsCollection =
                entityEventReference.get(networkStatusHelper.getSourceFrom()).await()
            entityEventsCollection.forEach { document ->
                val entityEvent = document.toObject<EntityEvent>()
                val event = eventDao.getEventById(entityEvent.eventId)
                if (event.second == FirestoreRespond.OK && event.first != null) {
                    val eventApplied = EventApplied(
                        id = document.id,
                        eventId = entityEvent.eventId,
                        date = entityEvent.date,
                        title = event.first?.title ?: "",
                        description = event.first?.description ?: "",
                    )
                    entityEvents.add(eventApplied)
                } else {
                    return Pair(emptyList(), event.second)
                }

            }
            Pair(entityEvents, FirestoreRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getEntityEvents: ${e.message}")
            Pair(emptyList(), FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun createEntityEvent(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ): FirestoreRespond {
        val entityEventReference = if (entityType == EntityType.Animal) {
            getAnimalEventsCollectionReference(entityId)
        } else {
            getLotEventsCollectionReference(entityId)
        } ?: return FirestoreRespond.NO_FARM_SESSION

        return try {
            entityEventReference.add(entityEvent).await()
            FirestoreRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in createEntityEvent: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun updateEntityEvent(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ): FirestoreRespond {
        val entityEventReference = if (entityType == EntityType.Animal) {
            getAnimalEventsCollectionReference(entityId)
        } else {
            getLotEventsCollectionReference(entityId)
        } ?: return FirestoreRespond.NO_FARM_SESSION

        return try {
            entityEvent.id?.let {
                entityEventReference.document(it).set(entityEvent).await()
                return FirestoreRespond.OK
            }
            FirestoreRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateEntityEvent: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun deleteEntityEventById(id: String, entityType: EntityType): FirestoreRespond {
        val entityEventReference = if (entityType == EntityType.Animal) {
            getAnimalEventsCollectionReference(id)
        } else {
            getLotEventsCollectionReference(id)
        } ?: return FirestoreRespond.NO_FARM_SESSION

        return try {
            entityEventReference.document(id).delete().await()
            FirestoreRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in deleteEntityEventById: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}