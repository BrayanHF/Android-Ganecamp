package com.ganecamp.data.firestore

import com.ganecamp.data.error.DataError
import com.ganecamp.data.firestore.util.FirestoreCollections
import com.ganecamp.data.firestore.util.getSourceFrom
import com.ganecamp.data.model.FirestoreAnimal
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
class AnimalDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getAnimalCollectionReference(): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        return farm?.id?.let { farmId ->
            db.collection(FirestoreCollections.FARM_COLLECTION).document(farmId)
                .collection(FirestoreCollections.ANIMAL_COLLECTION)
        }
    }

    suspend fun getAllAnimals(): DataResult<List<FirestoreAnimal>> {
        val animalsReference = getAnimalCollectionReference()
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val animals = mutableListOf<FirestoreAnimal>()
            val querySnapshot = animalsReference.get(networkStatusHelper.getSourceFrom()).await()

            querySnapshot.forEach { document ->
                val animal = document.toObject<FirestoreAnimal>()
                animal.id = document.id
                animals.add(animal)
            }
            DataResult.Success(animals)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getAnimalById(id: String): DataResult<FirestoreAnimal> {
        val animalsReference = getAnimalCollectionReference()
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val document =
                animalsReference.document(id).get(networkStatusHelper.getSourceFrom()).await()
            val animal = document.toObject<FirestoreAnimal>()

            animal?.let {
                animal.id = document.id
                DataResult.Success(animal)
            } ?: DataResult.Error(DataError.NotFoundError())
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getAnimalByTag(tag: String): DataResult<FirestoreAnimal> {
        val animalsReference = getAnimalCollectionReference()
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val querySnapshot =
                animalsReference.whereEqualTo("tag", tag).get(networkStatusHelper.getSourceFrom())
                    .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val animal = document.toObject<FirestoreAnimal>()
                animal?.let {
                    animal.id = document.id
                    return DataResult.Success(animal)
                }
            }
            DataResult.Error(DataError.NotFoundError())
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun addAnimal(animal: FirestoreAnimal): DataResult<String> {
        val animalsReference = getAnimalCollectionReference()
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            val documentReference = animalsReference.add(animal).await()
            DataResult.Success(documentReference.id)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun updateAnimal(animal: FirestoreAnimal): DataResult<Unit> {
        val animalsReference = getAnimalCollectionReference()
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            animalsReference.document(animal.id!!).set(animal).await()
            return DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun deleteAnimalById(id: String): DataResult<Unit> {
        val animalsReference = getAnimalCollectionReference()
            ?: return DataResult.Error(DataError.NoFarmSessionError())

        return try {
            animalsReference.document(id).delete().await()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}