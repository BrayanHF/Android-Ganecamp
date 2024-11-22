package com.ganecamp.di

import com.ganecamp.data.auth.AuthRepositoryImpl
import com.ganecamp.data.repository.AnimalRepositoryImpl
import com.ganecamp.data.repository.EventAppliedRepositoryImpl
import com.ganecamp.data.repository.EventRepositoryImpl
import com.ganecamp.data.repository.FarmRepositoryImpl
import com.ganecamp.data.repository.GanecampUserRepositoryImpl
import com.ganecamp.data.repository.LotRepositoryImpl
import com.ganecamp.data.repository.VaccineAppliedRepositoryImpl
import com.ganecamp.data.repository.VaccineRepositoryImpl
import com.ganecamp.data.repository.WeightRepositoryImpl
import com.ganecamp.domain.auth.Auth
import com.ganecamp.domain.repository.AnimalRepository
import com.ganecamp.domain.repository.EventAppliedRepository
import com.ganecamp.domain.repository.EventRepository
import com.ganecamp.domain.repository.FarmRepository
import com.ganecamp.domain.repository.GanecampUserRepository
import com.ganecamp.domain.repository.LotRepository
import com.ganecamp.domain.repository.VaccineAppliedRepository
import com.ganecamp.domain.repository.VaccineRepository
import com.ganecamp.domain.repository.WeightRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAutRepo(impl: AuthRepositoryImpl): Auth = impl

    @Provides
    fun provideAnimalRepo(impl: AnimalRepositoryImpl): AnimalRepository = impl

    @Provides
    fun provideEventAppliedRepo(impl: EventAppliedRepositoryImpl): EventAppliedRepository = impl

    @Provides
    fun provideEventRepo(impl: EventRepositoryImpl): EventRepository = impl

    @Provides
    fun provideFarmRepo(impl: FarmRepositoryImpl): FarmRepository = impl

    @Provides
    fun provideGanecampUserRepo(impl: GanecampUserRepositoryImpl): GanecampUserRepository = impl

    @Provides
    fun provideLotRepo(impl: LotRepositoryImpl): LotRepository = impl

    @Provides
    fun provideVaccineAppliedRepo(impl: VaccineAppliedRepositoryImpl): VaccineAppliedRepository = impl

    @Provides
    fun provideVaccineRepo(impl: VaccineRepositoryImpl): VaccineRepository = impl

    @Provides
    fun provideWeightRepo(impl: WeightRepositoryImpl): WeightRepository = impl

}