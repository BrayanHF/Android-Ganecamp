package com.ganecamp.domain.services

import com.ganecamp.data.database.dao.AnimalDao
import com.ganecamp.data.database.dao.AnimalLotDao
import com.ganecamp.data.database.entities.toEntity
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.domain.model.toDomain
import java.time.Period
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class AnimalService @Inject constructor(private val animalDao: AnimalDao, private val animalLotDao: AnimalLotDao) {

    suspend fun getAllAnimals(): List<Animal> {
        return animalDao.getAllAnimals().map { simpleAnimalData ->
            simpleAnimalData.toDomain()
        }
    }

    suspend fun getAnimalByTag(tag: String): AnimalDetail = animalDao.getAnimalByTag(tag).toDomain()

    suspend fun insertAnimal(animal: AnimalDetail) = animalDao.insertAnimal(animal.toEntity())

    suspend fun updateAnimal(animal: AnimalDetail) = animalDao.updateAnimal(animal.toEntity())

    suspend fun deleteAnimal(tag: String) = animalDao.deleteAnimalByTag(tag)

    suspend fun deleteAllAnimals() = animalDao.deleteAllAnimals()

    fun calculateAge(birthDate: ZonedDateTime): Triple<Int, Int, Int> {
        val today = ZonedDateTime.now()
        val birthInZone = birthDate.withZoneSameInstant(today.zone)

        val period = Period.between(birthInZone.toLocalDate(), today.toLocalDate())
        val days = ChronoUnit.DAYS.between(birthInZone, today).toInt()

        return Triple(period.years, period.months, days - period.years * 365 - period.months * 30)
    }

    suspend fun getAnimalById(animalId: Int): AnimalDetail =
        animalDao.getAnimalById(animalId).toDomain()

    suspend fun addLotToAnimal(animalId: Int, lotId: Int) = animalLotDao.addLotToAnimal(animalId, lotId)

    suspend fun changeLotToAnimal(animalId: Int, lotId: Int) = animalLotDao.changeLotToAnimal(animalId, lotId)

    suspend fun removeFromLot(animalId: Int) = animalLotDao.removeFromLot(animalId)

}