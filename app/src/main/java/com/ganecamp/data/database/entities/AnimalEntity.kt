package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.time.ZonedDateTime

@Entity(
    tableName = "animal_table",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class AnimalEntity(
    @PrimaryKey
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "gender") val gender: Gender,
    @ColumnInfo(name = "birth_date") val birthDate: ZonedDateTime,
    @ColumnInfo(name = "purchase_value") val purchaseValue: Double,
    @ColumnInfo(name = "sale_value") val saleValue: Double,
    @ColumnInfo(name = "state") val state: State
)

fun AnimalDetail.toEntity() = AnimalEntity(tag, id, gender, birthDate, purchaseValue, saleValue, state)
