package com.ganecamp.domain.model

import com.ganecamp.data.database.entities.AnimalEntity
import com.ganecamp.data.database.entities.LotEntity
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.time.ZonedDateTime

data class AnimalDetail(
    val tag: String,
    val id: String,
    val gender: Gender,
    val birthDate: ZonedDateTime,
    val purchaseValue: Double,
    val saleValue: Double,
    val state: State
)

fun AnimalEntity.toDomain() =
    AnimalDetail(tag, id, gender, birthDate, purchaseValue, saleValue, state)
