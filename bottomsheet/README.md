# Bottom Sheet Implementation

This document describes the bottom sheet implementation using Navigation 3 in this project.

## Overview

The bottom sheet feature has been implemented following the existing multi-module architecture pattern used in this project. It demonstrates how to use Material 3's `ModalBottomSheet` with Navigation 3's scene strategy system.

## Architecture

The implementation follows the same api/impl pattern as other features:

```
bottomsheet/
├── api/                          # Public navigation contracts
│   └── BottomSheetNavigation.kt # NavKey definition
└── impl/                         # Feature implementation
    ├── BottomSheetSceneStrategy.kt  # Scene strategy for bottom sheets
    ├── BottomSheetUi.kt            # UI composable
    └── BottomSheetModule.kt         # Hilt DI module
```

## Key Components

### 1. BottomSheetNavigation (API)
Defines the navigation contract:
```kotlin
object BottomSheetNavigation {
    object BottomSheet : NavKey
}
```

### 2. BottomSheetSceneStrategy
A custom `SceneStrategy` that renders navigation entries within a `ModalBottomSheet`. This strategy:
- Checks if an entry has bottom sheet metadata
- Creates an overlay scene that wraps the content in a `ModalBottomSheet`
- Handles dismissal through the `onBack` callback

### 3. BottomSheetUi
A simple Composable that displays:
- Title: "Nav3 Implementation"
- Description: "This is the showcase of Nav3 bottom sheet"

### 4. BottomSheetModule
Hilt module that registers the bottom sheet entry provider with:
- The `BottomSheet` NavKey
- Bottom sheet metadata
- The BottomSheetUi composable

## Integration

### MainActivity
Updated to include the `BottomSheetSceneStrategy`:
```kotlin
val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
NavDisplay(
    backStack = navigator.backStack,
    onBack = { navigator.goBack() },
    sceneStrategy = bottomSheetStrategy,
    entryProvider = entryProvider { ... }
)
```

### MainScreen
Updated to navigate to the bottom sheet on button click:
```kotlin
Button(onClick = { openBottomSheet() }) {
    Text("Open BottomSheet")
}
```

## Usage

When the "Open BottomSheet" button is clicked on the main screen:
1. Navigator adds `BottomSheetNavigation.BottomSheet` to the backstack
2. The BottomSheetSceneStrategy detects the bottom sheet metadata
3. Creates an overlay scene with `ModalBottomSheet`
4. Displays the BottomSheetUi content
5. User can dismiss by swiping down or tapping outside

## Reference

This implementation is based on the [android/nav3-recipes](https://github.com/android/nav3-recipes/) repository's bottom sheet example, adapted to fit this project's architecture.

## Build Note

**AGP Version Issue**: The original project had `agp = "8.13.2"` in `gradle/libs.versions.toml`, which is not a valid version. This has been updated to `8.1.3` which is compatible with Gradle 8.13.

To build the project, ensure you have:
- Android Studio Ladybug or newer
- JDK 11+
- Android SDK 36
- Network access to Google Maven repository
