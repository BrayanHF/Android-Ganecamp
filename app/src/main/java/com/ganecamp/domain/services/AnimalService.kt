package com.ganecamp.domain.services

import com.ganecamp.data.firibase.dao.AnimalDao
import com.ganecamp.model.objects.Animal
import com.google.firebase.Timestamp
import java.time.Period
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalService @Inject constructor(
    private val animalDao: AnimalDao
) {

    suspend fun getAllAnimals() = animalDao.getAllAnimals()

    suspend fun getAnimalById(id: String) = animalDao.getAnimalById(id)

    suspend fun getAnimalByTag(tag: String) = animalDao.getAnimalByTag(tag)

    suspend fun createAnimal(animal: Animal) = animalDao.createAnimal(animal)

    suspend fun updateAnimal(animal: Animal) = animalDao.updateAnimal(animal)

    suspend fun deleteAnimalByTag(tag: String) = animalDao.deleteAnimalByTag(tag)

    fun calculateAge(birthTimestamp: Timestamp): Triple<Int, Int, Int> {
        val birthDate = birthTimestamp.toDate().toInstant().atZone(ZonedDateTime.now().zone)
        val today = ZonedDateTime.now()
        val birthInZone = birthDate.withZoneSameInstant(today.zone)

        val period = Period.between(birthInZone.toLocalDate(), today.toLocalDate())
        val days = ChronoUnit.DAYS.between(birthInZone, today).toInt()

        return Triple(period.years, period.months, days - period.years * 365 - period.months * 30)
    }

}