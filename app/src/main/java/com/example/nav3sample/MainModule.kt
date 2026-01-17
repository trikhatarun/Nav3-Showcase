package com.example.nav3sample

import androidx.navigation3.runtime.NavKey
import com.example.bottomsheet.api.BottomSheetNavigation
import com.example.foundation.navigation.EntryProviderInstaller
import com.example.foundation.navigation.Navigator
import com.example.nav3sample.ui.MainScreen
import com.example.offers.api.OffersNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object MainNavigation {
    object Main : NavKey
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainModule {

    @IntoSet
    @Provides
    fun provideOffersEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<MainNavigation.Main> {
            MainScreen(
                openOffers = { navigator.goTo(OffersNavigation.Offers) },
                openBottomSheet = { navigator.goTo(BottomSheetNavigation.BottomSheet) }
            )
        }
    }
}