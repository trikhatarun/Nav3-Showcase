package com.example.profile.impl

import com.example.foundation.navigation.EntryProviderInstaller
import com.example.profile.api.ProfileNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object ProfileModule {
    @IntoSet
    @Provides
    fun provideProfileEntryProviderInstaller(): EntryProviderInstaller = {
        entry<ProfileNavigation.Profile> {
            ProfileUi()
        }
    }
}