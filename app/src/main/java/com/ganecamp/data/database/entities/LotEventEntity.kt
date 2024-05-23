package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(
    tableName = "lot_event_table",
    foreignKeys = [
        ForeignKey(
            entity = LotEntity::class,
            parentColumns = ["id"],
            childColumns = ["lot_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LotEventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lot_id") val lotId: Int,
    @ColumnInfo(name = "event_id") val eventId: Int,
    @ColumnInfo(name = "event_date") val applicationDate: ZonedDateTime
)