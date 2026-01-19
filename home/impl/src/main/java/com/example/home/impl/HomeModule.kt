package com.example.home.impl

import com.example.bottomsheet.api.BottomSheetNavigation
import com.example.foundation.navigation.EntryProviderInstaller
import com.example.foundation.navigation.Navigator
import com.example.home.api.HomeNavigation
import com.example.offers.api.OffersNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object HomeModule {

    @IntoSet
    @Provides
    fun provideHomeEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<HomeNavigation.Home> {
            HomeScreen(
                openOffers = { navigator.goTo(OffersNavigation.Offers) },
                openBottomSheet = { navigator.goTo(BottomSheetNavigation.BottomSheet) }
            )
        }
    }
}

