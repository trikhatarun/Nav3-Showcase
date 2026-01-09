package com.example.foundation.navigation

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.Multibinds

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class NavigationModule {

    /**
     * Declares that a Set<EntryProviderInstaller> multibinding exists.
     * Feature modules can contribute to this set using @IntoSet.
     * This allows the set to be empty if no features are included.
     */
    @Multibinds
    abstract fun bindEntryProviderInstallers(): Set<EntryProviderInstaller>
}

