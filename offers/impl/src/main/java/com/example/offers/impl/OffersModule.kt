package com.example.offers.impl

import com.example.foundation.navigation.EntryProviderInstaller
import com.example.offers.api.OffersNavigation
import com.example.offers.impl.details.OfferDetailsUi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object OffersModule {

    @IntoSet
    @Provides
    fun provideOffersEntryProviderInstaller() : EntryProviderInstaller = {
        entry<OffersNavigation.Offers>{
            OffersUi()
        }
    }

    @IntoSet
    @Provides
    fun provideOfferDetailsEntryProviderInstaller() : EntryProviderInstaller = {
        entry<OffersNavigation.OfferDetails>{ key ->
            OfferDetailsUi(id = key.id)
        }
    }
}