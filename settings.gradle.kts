pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Nav3 Sample"
include(":app")
include(":home:api")
include(":home:impl")
include(":offers:api")
include(":offers:impl")
include(":profile:api")
include(":profile:impl")
include(":foundation:navigation")
include(":foundation:design")
include(":bottomsheet:api")
include(":bottomsheet:impl")
