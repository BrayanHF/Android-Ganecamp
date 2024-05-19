package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "weight_table")
data class WeightEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "date") val date: ZonedDateTime,
    @ColumnInfo(name = "animal_id") val animalId: String
)