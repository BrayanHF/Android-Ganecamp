package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.time.ZonedDateTime

@Entity(
    tableName = "animal_table",
    foreignKeys = [
        ForeignKey(
            entity = LotEntity::class,
            parentColumns = ["id"],
            childColumns = ["lot_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
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
    @ColumnInfo(name = "purchase_value") val purchaseValue: Double?,
    @ColumnInfo(name = "sale_value") val saleValue: Double?,
    @ColumnInfo(name = "state") val state: State,
    @ColumnInfo(name = "lot_id") val lot: LotEntity?
)