package com.borja.android.supermarketapp.data.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    
    @Singleton
    @Provides
    fun provideFirestore() = Firebase.firestore

    @Singleton
    @Provides
    fun provideStorage() = Firebase.storage
}