package com.slaytertv.tcgbolivia.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.slaytertv.tcgbolivia.data.repository.AuthRepository
import com.slaytertv.tcgbolivia.data.repository.AuthRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    //para usar firestore y storage
    /*@Provides
    @Singleton
    fun provideNoteRepository(
        database: FirebaseFirestore,
        storageReference: StorageReference
    ): LaptopRepository {
        return LaptopRepositoryImp(database,storageReference)
    }*/
    /*
    //indicamos q nuestro task repositoey se use con imp
    @Provides
    @Singleton
    fun provideTaskRepository(
        database: FirebaseDatabase
    ): TaskRepository {
        return TaskRepositoryImp(database)
    }
    //para usar firebaseAuth, firestore,sharedpreferences y gson
    */

    //cuando se utilize auth tambien colocamos las dependencias q usaran
    @Provides
    @Singleton
    fun provideAutghRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
        gson: Gson
    ): AuthRepository {
        return AuthRepositoryImp(auth,database,appPreferences,gson)
    }
}