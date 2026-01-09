package com.example.nav3sample.di

import com.example.foundation.navigation.Navigator
import com.example.offers.api.OffersNavigation
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
    fun provideNavigator(): Navigator = Navigator(startDestination = OffersNavigation.Offers)
}

