package com.ju531.juencryptionmanager.di

import com.ju531.juencryptionmanager.EncryptionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object EncryptionModule {

    @Provides
    fun provideEncryptionManager() : EncryptionManager {
        return EncryptionManager()
    }
}