package com.example.offers.api

import com.example.foundation.navigation.EntryProviderInstaller
import com.example.foundation.navigation.Navigator
import com.example.offers.impl.OffersUi
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
    fun provideOffersEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<OffersNavigation.Offers> {
            OffersUi(
                onOfferClick = {
                    navigator.goTo(OffersNavigation.OfferDetails(it))
                }
            )
        }
    }

    @IntoSet
    @Provides
    fun provideOfferDetailsEntryProviderInstaller(): EntryProviderInstaller = {
        entry<OffersNavigation.OfferDetails> { key ->
            OfferDetailsUi(id = key.id)
        }
    }
}
