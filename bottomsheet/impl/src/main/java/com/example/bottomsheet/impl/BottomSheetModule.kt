package com.example.bottomsheet.impl

import com.example.bottomsheet.api.BottomSheetNavigation
import com.example.foundation.navigation.EntryProviderInstaller
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object BottomSheetModule {

    @IntoSet
    @Provides
    fun provideBottomSheetEntryProviderInstaller(): EntryProviderInstaller = {
        entry<BottomSheetNavigation.BottomSheet>(
            metadata = BottomSheetSceneStrategy.bottomSheet()
        ) {
            BottomSheetUi()
        }
    }
}
