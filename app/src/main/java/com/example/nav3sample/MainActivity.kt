package com.example.nav3sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.bottomsheet.impl.BottomSheetSceneStrategy
import com.example.design.Nav3SampleTheme
import com.example.foundation.navigation.EntryProviderInstaller
import com.example.foundation.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards EntryProviderInstaller>

    private var keepSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        lifecycleScope.launch {
            delay(500)
            keepSplashScreen = false
        }

        enableEdgeToEdge()
        setContent {
            Nav3SampleTheme {
                val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
                Scaffold { paddingValues ->
                    NavDisplay(
                        backStack = navigator.backStack,
                        modifier = Modifier.padding(paddingValues),
                        onBack = { navigator.goBack() },
                        sceneStrategy = bottomSheetStrategy,
                        entryProvider = entryProvider {
                            entryProviderScopes.forEach { builder ->
                                builder()
                            }
                        },
                    )
                }
            }
        }
    }
}