package com.example.nav3sample.di

import com.example.foundation.navigation.Navigator
import com.example.nav3sample.MainNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNavigator(): Navigator = Navigator(startDestination = MainNavigation.Main)
}

