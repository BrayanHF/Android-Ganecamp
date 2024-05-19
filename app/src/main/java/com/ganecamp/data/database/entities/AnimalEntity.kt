package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_table")
data class AnimalEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "id") val id: String,
    val weights: List<WeightEntity>, // TODO
    @ColumnInfo(name = "vaccines") val vaccines: String, // TODO
    @ColumnInfo(name = "state") val state: String, // TODO
    @ColumnInfo(name = "lot") val lot: String, // TODO
    @ColumnInfo(name = "purchase_value") val purchaseValue: Double?,
    @ColumnInfo(name = "sale_value") val saleValue: Double?,
    @ColumnInfo(name = "news") val news: String // TODO
)