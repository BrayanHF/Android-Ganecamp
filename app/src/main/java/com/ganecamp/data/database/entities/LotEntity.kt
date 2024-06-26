package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ganecamp.domain.model.LotDetail
import java.time.ZonedDateTime

@Entity(tableName = "lot_table")
data class LotEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "purchase_value") val purchaseValue: Double,
    @ColumnInfo(name = "purchase_date") val purchaseDate: ZonedDateTime,
    @ColumnInfo(name = "sale_value") val saleValue: Double,
    @ColumnInfo(name = "sale_date") val saleDate: ZonedDateTime
)

fun LotDetail.toEntity() = LotEntity(id, purchaseValue, purchaseDate, saleValue, saleDate)