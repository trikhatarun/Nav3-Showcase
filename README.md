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
├── app/                          # Application module (entry point, MainActivity only)
├── foundation/
│   ├── design/                   # Shared design system (Theme, Colors, Typography)
│   └── navigation/               # Core navigation utilities (Navigator, EntryProviderInstaller)
├── home/
│   ├── api/                      # Public navigation contracts (HomeNavigation.Home)
│   └── impl/                     # Home screen implementation (UI, DI)
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

    subgraph "Feature: Home"
        HOME_API[":home:api"]
        HOME_IMPL[":home:impl"]
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
    APP --> HOME_API
    APP --> HOME_IMPL
    APP --> OFFERS_IMPL
    APP --> PROFILE_IMPL

    HOME_API --> NAV
    HOME_IMPL --> HOME_API
    HOME_IMPL --> NAV
    HOME_IMPL --> DESIGN
    HOME_IMPL --> OFFERS_API

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
    style HOME_API fill:#e3f2fd
    style HOME_IMPL fill:#bbdefb
    style OFFERS_API fill:#e8f5e9
    style OFFERS_IMPL fill:#c8e6c9
    style PROFILE_API fill:#fce4ec
    style PROFILE_IMPL fill:#f8bbd9
```

> **Key Insight**: Notice how `:home:impl` depends on `:offers:api` (not `:offers:impl`), and `:offers:impl` depends on `:profile:api` (not `:profile:impl`). This ensures feature modules can navigate to each other without accessing implementation details.

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
            │  HomeModule  │ │ OffersModule │ │ ProfileModule│
            │   @IntoSet   │ │   @IntoSet   │ │   @IntoSet   │
            │   installer  │ │   installer  │ │   installer  │
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

**4. Feature Module Registration** (`home:impl`)

Each feature module provides its entry installers using `@IntoSet`:

```kotlin
@Module
@InstallIn(ActivityRetainedComponent::class)
object HomeModule {

    @IntoSet
    @Provides
    fun provideHomeEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<HomeNavigation.Home> {
            HomeScreen(
                openOffers = { navigator.goTo(OffersNavigation.Offers) }
            )
        }
    }
}
```

**5. NavKey Definitions** (`home:api`, `offers:api`)

API modules expose only the navigation contracts:

```kotlin
// home/api/HomeNavigation.kt
object HomeNavigation {
    object Home : NavKey
}

// offers/api/OffersNavigation.kt
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

### 2. 🚧 Under Construction

> **Coming Soon**: Additional navigation patterns and use cases will be documented here.

*Planned topics may include:*
- Bottom sheet navigation
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
| `home/api/HomeNavigation.kt` | Public `NavKey` definitions for home feature |
| `home/impl/HomeModule.kt` | Hilt module registering home entry providers |
| `home/impl/HomeScreen.kt` | Home screen UI implementation |
| `offers/api/OffersNavigation.kt` | Public `NavKey` definitions for offers feature |
| `offers/impl/OffersModule.kt` | Hilt module registering offers entry providers |
| `app/MainActivity.kt` | Entry point that assembles all navigation entries |

---

## 📄 License

```
Copyright 2025

Licensed under the Apache License, Version 2.0
```

