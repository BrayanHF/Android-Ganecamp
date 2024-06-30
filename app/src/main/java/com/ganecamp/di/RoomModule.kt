package com.ganecamp.di

import android.content.Context
import androidx.room.Room
import com.ganecamp.data.database.GanecampDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "ganecamp_database"

    @Singleton
    @Provides
    fun provideGanecampDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, GanecampDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideAnimalDao(ganecampDatabase: GanecampDatabase) = ganecampDatabase.getAnimalDao()

    @Singleton
    @Provides
    fun provideLotDao(ganecampDatabase: GanecampDatabase) = ganecampDatabase.getLotDao()

    @Singleton
    @Provides
    fun provideVaccineDao(ganecampDatabase: GanecampDatabase) = ganecampDatabase.getVaccineDao()

    @Singleton
    @Provides
    fun provideEventDao(ganecampDatabase: GanecampDatabase) = ganecampDatabase.getEventDao()

    @Singleton
    @Provides
    fun provideWeightDao(ganecampDatabase: GanecampDatabase) = ganecampDatabase.getWeightDao()

}