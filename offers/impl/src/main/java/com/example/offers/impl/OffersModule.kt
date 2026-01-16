package com.example.offers.impl

import com.example.foundation.navigation.EntryProviderInstaller
import com.example.foundation.navigation.Navigator
import com.example.offers.api.OffersNavigation
import com.example.offers.impl.details.OfferDetailsUi
import com.example.profile.api.ProfileNavigation
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
    fun provideOfferDetailsEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<OffersNavigation.OfferDetails> { key ->
            OfferDetailsUi(
                id = key.id,
                onProfileClick = {
                    navigator.goTo(ProfileNavigation.Profile)
                }
            )
        }
    }
}

