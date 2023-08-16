package com.slaytertv.tcgbolivia.di

import com.slaytertv.tcgbolivia.data.repository.YgoProDeckApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://db.ygoprodeck.com/api/v7/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideYgoProDeckApiService(retrofit: Retrofit): YgoProDeckApiService {
        return retrofit.create(YgoProDeckApiService::class.java)
    }
}
