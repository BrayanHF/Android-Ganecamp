package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "animal_lot_table",
    foreignKeys = [
        ForeignKey(
            entity = AnimalEntity::class,
            parentColumns = ["tag"],
            childColumns = ["animal_tag"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LotEntity::class,
            parentColumns = ["id"],
            childColumns = ["lot_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnimalLotEntity(
    @PrimaryKey
    @ColumnInfo(name = "animal_tag") val animalTag: String,
    @ColumnInfo(name = "lot_id") val lotId: String
)