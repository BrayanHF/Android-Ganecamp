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
            parentColumns = ["id"],
            childColumns = ["animal_id"],
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "animal_id") val animalId: Int,
    @ColumnInfo(name = "vaccine_id") val vaccineId: Int,
    @ColumnInfo(name = "application_date") val applicationDate: ZonedDateTime
)