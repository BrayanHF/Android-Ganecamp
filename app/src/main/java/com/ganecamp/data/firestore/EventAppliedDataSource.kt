package com.ganecamp.data.firestore

import com.ganecamp.data.error.DataError
import com.ganecamp.domain.session.FarmSessionManager
import com.ganecamp.data.firestore.util.FirestoreCollections
import com.ganecamp.data.firestore.util.getSourceFrom
import com.ganecamp.data.model.FirestoreEntityEvent
import com.ganecamp.data.model.FirestoreEventApplied
import com.ganecamp.data.network.NetworkStatusHelper
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.enums.EntityType
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventAppliedDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val eventDao: EventDataSource,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getAnimalEventsCollectionReference(animalId: String): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        return farm?.id?.let {
            db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.ANIMAL_COLLECTION).document(animalId)
                .collection(FirestoreCollections.ANIMAL_EVENT_COLLECTION)
        }
    }

    private fun getLotEventsCollectionReference(lotId: String): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        return farm?.id?.let {
            db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.LOT_COLLECTION).document(lotId)
                .collection(FirestoreCollections.LOT_EVENT_COLLECTION)
        }
    }

    suspend fun getEntityEvents(
        entityId: String, entityType: EntityType
    ): DataResult<List<FirestoreEventApplied>> {

        val entityEventsReference = if (entityType == EntityType.Animal) {
            getAnimalEventsCollectionReference(entityId)
        } else {
            getLotEventsCollectionReference(entityId)
        } ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val entityEvents = mutableListOf<FirestoreEventApplied>()
            val entityEventsCollection =
                entityEventsReference.get(networkStatusHelper.getSourceFrom()).await()

            entityEventsCollection.forEach { document ->
                val entityEvent = document.toObject<FirestoreEntityEvent>()
                when (val eventResult = eventDao.getFarmEventById(entityEvent.eventId)) {
                    is DataResult.Success -> {
                        val eventApplied = FirestoreEventApplied(
                            id = document.id,
                            eventId = entityEvent.eventId,
                            date = entityEvent.date,
                            title = eventResult.data.title,
                            description = eventResult.data.description
                        )
                        entityEvents.add(eventApplied)
                    }
                    is DataResult.Error -> return DataResult.Error(eventResult.exception)
                }
            }
            DataResult.Success(entityEvents)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun addEntityEvent(
        entityId: String, entityEvent: FirestoreEntityEvent, entityType: EntityType
    ): DataResult<String> {

        val entityEventReference = if (entityType == EntityType.Animal) {
            getAnimalEventsCollectionReference(entityId)
        } else {
            getLotEventsCollectionReference(entityId)
        } ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val documentReference = entityEventReference.add(entityEvent).await()
            DataResult.Success(documentReference.id)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun updateEntityEvent(
        entityId: String, entityEvent: FirestoreEntityEvent, entityType: EntityType
    ): DataResult<Unit> {

        val entityEventReference = if (entityType == EntityType.Animal) {
            getAnimalEventsCollectionReference(entityId)
        } else {
            getLotEventsCollectionReference(entityId)
        } ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            entityEventReference.document(entityEvent.id!!).set(entityEvent).await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun deleteEntityEventById(id: String, entityType: EntityType): DataResult<Unit> {
        val entityEventReference = if (entityType == EntityType.Animal) {
            getAnimalEventsCollectionReference(id)
        } else {
            getLotEventsCollectionReference(id)
        } ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            entityEventReference.document(id).delete().await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}