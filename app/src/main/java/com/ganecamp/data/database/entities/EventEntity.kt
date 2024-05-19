package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "event_table")
data class EventEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String
)