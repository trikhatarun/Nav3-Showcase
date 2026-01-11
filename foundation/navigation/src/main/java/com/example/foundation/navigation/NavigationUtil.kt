package com.example.foundation.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.scopes.ActivityRetainedScoped


typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit

@ActivityRetainedScoped
class Navigator(startDestination: NavKey) {
    val backStack : SnapshotStateList<NavKey> = mutableStateListOf(startDestination)

    fun goTo(destination: NavKey){
        backStack.add(destination)
    }

    fun goBack(){
        backStack.removeLastOrNull()
    }
}