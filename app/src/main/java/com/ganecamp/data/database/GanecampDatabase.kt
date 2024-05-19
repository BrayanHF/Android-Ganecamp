package com.ganecamp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ganecamp.data.database.dao.AnimalDao
import com.ganecamp.data.database.entities.AnimalEntity

@Database(entities = [AnimalEntity::class], version = 1)
abstract class GanecampDatabase : RoomDatabase() {

    abstract fun getAnimalDao(): AnimalDao

}