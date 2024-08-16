package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ganecamp.domain.model.Weight
import java.time.ZonedDateTime

@Entity(
    tableName = "weight_table", foreignKeys = [ForeignKey(
        entity = AnimalEntity::class,
        parentColumns = ["id"],
        childColumns = ["animal_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class WeightEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "animal_id") val animalId: Int,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "date") val date: ZonedDateTime
)

fun Weight.toEntity() = WeightEntity(id, animalId, weight, date)