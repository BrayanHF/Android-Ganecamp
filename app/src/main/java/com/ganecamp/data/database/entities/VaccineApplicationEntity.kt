package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(
    tableName = "animal_vaccine_table",
    foreignKeys = [
        ForeignKey(
            entity = AnimalEntity::class,
            parentColumns = ["tag"],
            childColumns = ["animal_tag"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VaccineEntity::class,
            parentColumns = ["id"],
            childColumns = ["vaccine_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VaccineApplicationEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "animal_tag") val animalId: String,
    @ColumnInfo(name = "vaccine_id") val vaccineId: String,
    @ColumnInfo(name = "application_date") val applicationDate: ZonedDateTime
)