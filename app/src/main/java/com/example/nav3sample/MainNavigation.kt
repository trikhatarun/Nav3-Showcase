package com.example.nav3sample

import com.example.foundation.navigation.EntryProviderInstaller
import com.example.foundation.navigation.Navigator
import com.example.offers.api.OffersNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object MainNavigation {
    object Main
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object OffersModule {

    @IntoSet
    @Provides
    fun provideOffersEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<MainNavigation.Main> {
            MainScreen({
                navigator.goTo(OffersNavigation.Offers)
            })
        }
    }
}