package com.ahargunyllib.growth.di

import com.ahargunyllib.growth.contract.AuthRepository
import com.ahargunyllib.growth.contract.CollectionRepository
import com.ahargunyllib.growth.contract.MissionRepository
import com.ahargunyllib.growth.contract.PartnerRepository
import com.ahargunyllib.growth.contract.PointRepository
import com.ahargunyllib.growth.repository.AuthRepositoryImpl
import com.ahargunyllib.growth.repository.CollectionRepositoryImpl
import com.ahargunyllib.growth.repository.MissionRepositoryImpl
import com.ahargunyllib.growth.repository.PartnerRepositoryImpl
import com.ahargunyllib.growth.repository.PointRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCollectionRepository(
        collectionRepositoryImpl: CollectionRepositoryImpl
    ): CollectionRepository

    @Binds
    @Singleton
    abstract fun bindMissionRepository(
        missionRepositoryImpl: MissionRepositoryImpl
    ): MissionRepository

    @Binds
    @Singleton
    abstract fun bindPartnerRepository(
        partnerRepositoryImpl: PartnerRepositoryImpl
    ): PartnerRepository

    @Binds
    @Singleton
    abstract fun bindPointRepository(
        pointRepositoryImpl: PointRepositoryImpl
    ): PointRepository
}