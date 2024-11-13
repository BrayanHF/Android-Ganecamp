package com.ganecamp.data.firibase.dao

import android.util.Log
import com.ganecamp.data.firibase.FarmSessionManager
import com.ganecamp.data.firibase.FirestoreCollections
import com.ganecamp.data.firibase.getSourceFrom
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.domain.network.NetworkStatusHelper
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.functions.FirestoreErrorEvaluator
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalDao @Inject constructor(
    private val db: FirebaseFirestore,
    private val farmSessionManager: FarmSessionManager,
    private val networkStatusHelper: NetworkStatusHelper
) {

    private fun getAnimalCollectionReference(): CollectionReference? {
        val farm = farmSessionManager.getFarm()
        farm?.id?.let {
            return db.collection(FirestoreCollections.FARM_COLLECTION).document(it)
                .collection(FirestoreCollections.ANIMAL_COLLECTION)
        }
        return null
    }

    suspend fun getAllAnimals(): Pair<List<Animal>, FirestoreRespond> {
        val animalReference = getAnimalCollectionReference() ?: return Pair(
            emptyList(), FirestoreRespond.NO_FARM_SESSION
        )
        return try {
            val animals = mutableListOf<Animal>()
            val querySnapshot = animalReference.get(networkStatusHelper.getSourceFrom()).await()
            querySnapshot.forEach { document ->
                val animal = document.toObject<Animal>()
                animal.id = document.id
                animals.add(animal)
            }
            Pair(animals, FirestoreRespond.OK)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getAllAnimals: ${e.message}")
            Pair(emptyList(), FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun getAnimalById(id: String): Pair<Animal?, FirestoreRespond> {
        val animalCollectionReference =
            getAnimalCollectionReference() ?: return Pair(null, FirestoreRespond.NO_FARM_SESSION)

        return try {
            val document =
                animalCollectionReference.document(id).get(networkStatusHelper.getSourceFrom())
                    .await()
            val animal = document.toObject<Animal>()
            animal?.let {
                it.id = document.id
                return Pair(animal, FirestoreRespond.OK)
            }
            Pair(null, FirestoreRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getAnimalById: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun getAnimalByTag(tag: String): Pair<Animal?, FirestoreRespond> {
        val animalCollectionReference =
            getAnimalCollectionReference() ?: return Pair(null, FirestoreRespond.NO_FARM_SESSION)

        return try {
            val querySnapshot = animalCollectionReference.whereEqualTo("tag", tag)
                .get(networkStatusHelper.getSourceFrom()).await()
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val animal = document.toObject<Animal>()
                animal?.let {
                    animal.id = document.id
                    return Pair(animal, FirestoreRespond.OK)
                }
            }
            Pair(null, FirestoreRespond.NOT_FOUND)
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in getAnimalByTag: ${e.message}")
            Pair(null, FirestoreErrorEvaluator.evaluateError(e))
        }
    }

    suspend fun createAnimal(animal: Animal): FirestoreRespond {
        val animalCollectionReference =
            getAnimalCollectionReference() ?: return FirestoreRespond.NO_FARM_SESSION
        return try {
            animalCollectionReference.add(animal).await()
            FirestoreRespond.OK
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in createAnimal: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun updateAnimal(animal: Animal): FirestoreRespond {
        val animalCollectionReference =
            getAnimalCollectionReference() ?: return FirestoreRespond.NO_FARM_SESSION
        return try {
            animal.id?.let {
                animalCollectionReference.document(it).set(animal).await()
                return FirestoreRespond.OK
            }
            FirestoreRespond.NULL_POINTER
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in updateAnimal: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun deleteAnimalByTag(tag: String): FirestoreRespond {
        val animalCollectionReference =
            getAnimalCollectionReference() ?: return FirestoreRespond.NO_FARM_SESSION

        return try {
            val animal = getAnimalByTag(tag)
            animal.first?.id?.let {
                animalCollectionReference.document(it).delete().await()
                return FirestoreRespond.OK
            }
            FirestoreRespond.NOT_FOUND
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in deleteAnimalByTag: ${e.message}")
            FirestoreErrorEvaluator.evaluateError(e)
        }
    }

}