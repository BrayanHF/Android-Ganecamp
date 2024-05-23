package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaccine_table")
data class VaccineEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String
)