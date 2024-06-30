package com.ganecamp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ganecamp.data.database.converters.DateConverter
import com.ganecamp.data.database.dao.AnimalDao
import com.ganecamp.data.database.dao.AnimalLotDao
import com.ganecamp.data.database.dao.EventDao
import com.ganecamp.data.database.dao.LotDao
import com.ganecamp.data.database.dao.VaccineDao
import com.ganecamp.data.database.dao.WeightDao
import com.ganecamp.data.database.entities.AnimalEntity
import com.ganecamp.data.database.entities.AnimalEventEntity
import com.ganecamp.data.database.entities.AnimalLotEntity
import com.ganecamp.data.database.entities.EventEntity
import com.ganecamp.data.database.entities.LotEntity
import com.ganecamp.data.database.entities.LotEventEntity
import com.ganecamp.data.database.entities.VaccineApplicationEntity
import com.ganecamp.data.database.entities.VaccineEntity
import com.ganecamp.data.database.entities.WeightEntity

@Database(
    entities = [
        AnimalEntity::class,
        AnimalEventEntity::class,
        AnimalLotEntity::class,
        EventEntity::class,
        LotEntity::class,
        LotEventEntity::class,
        VaccineApplicationEntity::class,
        VaccineEntity::class,
        WeightEntity::class
    ],
    version = 3
)
@TypeConverters(DateConverter::class)
abstract class GanecampDatabase : RoomDatabase() {

    abstract fun getAnimalDao(): AnimalDao
    abstract fun getLotDao(): LotDao
    abstract fun getVaccineDao(): VaccineDao
    abstract fun getEventDao(): EventDao
    abstract fun getWeightDao(): WeightDao
    abstract fun getAnimalLotDao(): AnimalLotDao

}