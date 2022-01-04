package com.nsa.edvoraassessmentapp.di

import com.nsa.edvoraassessmentapp.api.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetofit():Retrofit=
        Retrofit.Builder()
            .baseUrl("https://assessment-edvora.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit):ApiInterface =
        retrofit.create(ApiInterface::class.java)
}