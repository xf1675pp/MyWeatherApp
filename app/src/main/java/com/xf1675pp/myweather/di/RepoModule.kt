package com.xf1675pp.myweather.di

import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepoModule {
    @Provides
    @ViewModelScoped
    fun provideOpenWeatherMapRepo(

    ): OpenWeatherMapRepo
    {
        return OpenWeatherMapRepo(OpenWeatherMapInterface.create())
    }

    @Provides
    @ViewModelScoped
    fun provideOpenWeatherMapInterface(

    ): OpenWeatherMapInterface
    {
        return OpenWeatherMapInterface.create()
    }
}