package com.slaytertv.tcgbolivia.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.slaytertv.tcgbolivia.data.repository.AuthRepository
import com.slaytertv.tcgbolivia.ui.viewmodel.splash.SplashViewModelFactory
import com.slaytertv.tcgbolivia.util.FirebaseStorageConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FireabaseModul {
    //para usar firestore
    @Provides
    @Singleton
    fun provideFireStoreinstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun provideFireStoreMessage(): FirebaseMessaging{
        return FirebaseMessaging.getInstance()
    }
    //para usar auth se usa junto con repositoeymodule
    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    //para usar storage desde repository module
    @Singleton
    @Provides
    fun provideFirebaseStroageInstance(): StorageReference {
        return FirebaseStorage.getInstance().getReference(FirebaseStorageConstants.ROOT_DIRECTORY)
    }

    //para usar realtimedatabase desde repository modle
    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    //SPLASH
    @Provides
    @Singleton
    fun provideSplashViewModelFactory(auth: AuthRepository): SplashViewModelFactory {
        return SplashViewModelFactory(auth)
    }
}