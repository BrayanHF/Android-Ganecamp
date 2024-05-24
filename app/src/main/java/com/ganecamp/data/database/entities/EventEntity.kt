package com.ganecamp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ganecamp.domain.model.Event

@Entity(tableName = "event_table")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String
)

fun Event.toEntity() = EventEntity(id, title, description)