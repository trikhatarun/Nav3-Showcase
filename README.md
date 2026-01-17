# Navigation 3 Multi-Module Sample

A sample Android project demonstrating **Jetpack Navigation 3** with **Hilt Dependency Injection** in a multi-module architecture. This project showcases best practices for modular navigation where feature modules remain isolated and only have access to the dependencies they truly need.

## 🛠 Tech Stack

- **Jetpack Navigation 3** - Type-safe navigation with `NavKey`
- **Hilt** - Dependency injection with multibinding support
- **Jetpack Compose** - Modern declarative UI
- **Multi-Module Architecture** - Clean separation with api/impl pattern

## 📦 Module Structure

```
Nav3Sample/
├── app/                          # Application module (entry point)
├── foundation/
│   ├── design/                   # Shared design system (Theme, Colors, Typography)
│   └── navigation/               # Core navigation utilities (Navigator, EntryProviderInstaller)
├── offers/
│   ├── api/                      # Public navigation contracts (NavKeys)
│   └── impl/                     # Feature implementation (UI, ViewModels, DI)
└── profile/
    ├── api/                      # Public navigation contracts (NavKeys)
    └── impl/                     # Feature implementation (UI, ViewModels, DI)
```

### Module Dependency Diagram

```mermaid
graph TD
    subgraph "Application Layer"
        APP[":app"]
    end

    subgraph "Foundation Layer"
        NAV[":foundation:navigation"]
        DESIGN[":foundation:design"]
    end

    subgraph "Feature: Offers"
        OFFERS_API[":offers:api"]
        OFFERS_IMPL[":offers:impl"]
    end

    subgraph "Feature: Profile"
        PROFILE_API[":profile:api"]
        PROFILE_IMPL[":profile:impl"]
    end

    APP --> NAV
    APP --> DESIGN
    APP --> OFFERS_API
    APP --> OFFERS_IMPL
    APP --> PROFILE_IMPL

    OFFERS_API --> NAV
    OFFERS_IMPL --> OFFERS_API
    OFFERS_IMPL --> NAV
    OFFERS_IMPL --> DESIGN
    OFFERS_IMPL --> PROFILE_API

    PROFILE_API --> NAV
    PROFILE_IMPL --> PROFILE_API
    PROFILE_IMPL --> NAV
    PROFILE_IMPL --> DESIGN

    style APP fill:#e1f5fe
    style NAV fill:#fff3e0
    style DESIGN fill:#fff3e0
    style OFFERS_API fill:#e8f5e9
    style OFFERS_IMPL fill:#c8e6c9
    style PROFILE_API fill:#fce4ec
    style PROFILE_IMPL fill:#f8bbd9
```

> **Key Insight**: Notice how `:offers:impl` only depends on `:profile:api` (not `:profile:impl`). This ensures feature modules can navigate to each other without accessing implementation details.

---

## 📚 Use Cases

### 1. Multi-Module Navigation with Hilt Multibinding

This use case demonstrates the **ideal multi-module navigation pattern** where:
- Feature modules are isolated and only depend on what they need
- Navigation destinations are registered via Hilt's multibinding
- Modules expose only `NavKey` contracts through their API modules

#### Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              MainActivity                               │
│  ┌───────────────────────────────────────────────────────────────────┐  │
│  │                           NavDisplay                              │  │
│  │                               │                                   │  │
│  │                    ┌──────────┴──────────┐                        │  │
│  │                    ▼                     ▼                        │  │
│  │         Set<EntryProviderInstaller>      Navigator                │  │
│  │         (collected via multibinding)     (backstack management)   │  │
│  └───────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    ▼               ▼               ▼
            ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
            │ OffersModule │ │ ProfileModule│ │  MainModule  │
            │   @IntoSet   │ │   @IntoSet   │ │   @IntoSet   │
            │  installer   │ │   installer  │ │   installer  │
            └──────────────┘ └──────────────┘ └──────────────┘
```

#### Key Components

**1. Navigator Class** (`foundation:navigation`)

The `Navigator` is the central backstack manager, scoped to `ActivityRetainedComponent`:

```kotlin
@ActivityRetainedScoped
class Navigator(startDestination: NavKey) {
    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(startDestination)

    fun goTo(destination: NavKey) {
        backStack.add(destination)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}
```

**2. EntryProviderInstaller** (`foundation:navigation`)

A typealias that allows feature modules to register their navigation entries:

```kotlin
typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit
```

**3. Navigation Module with Multibinding** (`foundation:navigation`)

Declares an empty set that feature modules contribute to:

```kotlin
@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class NavigationModule {
    @Multibinds
    abstract fun bindEntryProviderInstallers(): Set<EntryProviderInstaller>
}
```

**4. Feature Module Registration** (`offers:impl`)

Each feature module provides its entry installers using `@IntoSet`:

```kotlin
@Module
@InstallIn(ActivityRetainedComponent::class)
object OffersModule {

    @IntoSet
    @Provides
    fun provideOffersEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<OffersNavigation.Offers> {
            OffersUi(
                onOfferClick = { navigator.goTo(OffersNavigation.OfferDetails(it)) }
            )
        }
    }
}
```

**5. NavKey Definitions** (`offers:api`)

API modules expose only the navigation contracts:

```kotlin
object OffersNavigation {
    object Offers : NavKey
    data class OfferDetails(val id: Int) : NavKey
}
```

**6. Consuming in MainActivity** (`app`)

All installers are collected and applied to the `NavDisplay`:

```kotlin
@Inject
lateinit var entryProviderScopes: Set<@JvmSuppressWildcards EntryProviderInstaller>

NavDisplay(
    backStack = navigator.backStack,
    onBack = { navigator.goBack() },
    entryProvider = entryProvider {
        entryProviderScopes.forEach { builder -> builder() }
    },
)
```

#### Benefits of This Approach

| Benefit | Description |
|---------|-------------|
| **Module Isolation** | Feature implementations don't know about each other |
| **Minimal API Surface** | Only `NavKey` objects are exposed publicly |
| **Compile-Time Safety** | Type-safe navigation with `NavKey` classes |
| **Easy Feature Addition** | New features just add `@IntoSet` providers |
| **Testability** | Each module can be tested independently |

---

### 2. Bottom Sheet Navigation with Scene Strategy

This use case demonstrates how to use **Material 3 Modal Bottom Sheets** with Navigation 3's scene strategy system.

#### Overview

The bottom sheet implementation uses a custom `SceneStrategy` to intercept navigation entries marked with bottom sheet metadata and render them inside a `ModalBottomSheet` instead of the default full-screen layout.

```
┌─────────────────────────────────────────────────────────────┐
│                        MainActivity                         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │                      NavDisplay                       │  │
│  │  sceneStrategy = BottomSheetSceneStrategy             │  │
│  │           │                                           │  │
│  │           ▼                                           │  │
│  │  Checks entry.metadata for "bottomsheet" key         │  │
│  │           │                                           │  │
│  │      ┌────┴────┐                                     │  │
│  │      ▼         ▼                                     │  │
│  │   Found    Not Found                                 │  │
│  │     │          │                                     │  │
│  │     ▼          ▼                                     │  │
│  │  Render in   Normal                                  │  │
│  │  ModalBottom  Scene                                  │  │
│  │  Sheet                                               │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

#### Key Components

**1. BottomSheetSceneStrategy**

A custom `SceneStrategy<T>` that:
- Inspects each navigation entry's metadata for a bottom sheet marker
- If found, creates a `BottomSheetScene` (an `OverlayScene`)
- If not found, returns null to let other strategies handle it

```kotlin
class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull()
        val bottomSheetProperties = lastEntry?.metadata?.get(BOTTOM_SHEET_KEY) 
            as? ModalBottomSheetProperties
        return bottomSheetProperties?.let { properties ->
            BottomSheetScene(
                key = lastEntry.contentKey as T,
                entry = lastEntry,
                modalBottomSheetProperties = properties,
                onBack = onBack
            )
        }
    }
}
```

**2. BottomSheetScene**

An `OverlayScene<T>` that wraps the entry's content in a `ModalBottomSheet`:

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
internal class BottomSheetScene<T : Any>(
    override val entries: List<NavEntry<T>>,
    private val onBack: () -> Unit,
) : OverlayScene<T> {
    override val content: @Composable (() -> Unit) = {
        ModalBottomSheet(onDismissRequest = onBack) {
            entry.Content()
        }
    }
}
```

**3. Entry Registration with Metadata**

Feature modules mark entries for bottom sheet display using metadata:

```kotlin
@Module
@InstallIn(ActivityRetainedComponent::class)
object BottomSheetModule {
    @IntoSet
    @Provides
    fun provideBottomSheetEntryProviderInstaller(): EntryProviderInstaller = {
        entry<BottomSheetNavigation.BottomSheet>(
            metadata = BottomSheetSceneStrategy.bottomSheet()  // ← Metadata marker
        ) {
            BottomSheetUi()
        }
    }
}
```

**4. MainActivity Integration**

The strategy must be registered with `NavDisplay`:

```kotlin
setContent {
    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        sceneStrategy = bottomSheetStrategy,  // ← Register strategy
        entryProvider = entryProvider { ... }
    )
}
```

#### Module Structure

```
bottomsheet/
├── api/                              # Public contracts
│   └── BottomSheetNavigation.kt      # NavKey definitions
└── impl/                             # Implementation
    ├── BottomSheetSceneStrategy.kt   # Scene strategy
    ├── BottomSheetUi.kt             # UI composable
    └── BottomSheetModule.kt          # Hilt registration
```

#### Benefits of Scene Strategy Approach

| Benefit | Description |
|---------|-------------|
| **Reusable** | Any entry can become a bottom sheet by adding metadata |
| **Type-safe** | Compile-time checked navigation |
| **Composable** | Multiple strategies can coexist |
| **Isolated** | Bottom sheet logic separated from navigation logic |
| **Flexible** | Easy to customize bottom sheet properties per entry |

#### Usage

From any screen with access to `Navigator`:
```kotlin
navigator.goTo(BottomSheetNavigation.BottomSheet)
```

The user can dismiss by:
- Swiping down
- Tapping outside the sheet
- Pressing back button

All trigger `navigator.goBack()` automatically.

---

### 3. 🚧 Under Construction

> **Coming Soon**: Additional navigation patterns and use cases will be documented here.

*Planned topics may include:*
- Nested navigation graphs
- Deep linking
- Shared element transitions

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug or newer
- JDK 11+
- Android SDK 36

### Build & Run

```bash
# Clone the repository
git clone <repository-url>

# Build the project
./gradlew assembleDebug

# Run on connected device/emulator
./gradlew installDebug
```

---

## 📁 Key Files Reference

| File | Description |
|------|-------------|
| `foundation/navigation/NavigationUtil.kt` | `Navigator` class and `EntryProviderInstaller` typealias |
| `foundation/navigation/NavigationModule.kt` | Hilt module with `@Multibinds` declaration |
| `offers/api/OffersNavigation.kt` | Public `NavKey` definitions for offers feature |
| `offers/impl/OffersModule.kt` | Hilt module registering offers entry providers |
| `app/MainActivity.kt` | Entry point that assembles all navigation entries |

---

## 📄 License

```
Copyright 2025

Licensed under the Apache License, Version 2.0
```

