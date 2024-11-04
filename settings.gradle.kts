pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com.android.*")
                includeGroupByRegex("com.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com.android.*")
                includeGroupByRegex("com.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "Diary"

include(":app:platform:jvm")
include(":app:platform:wasm")
include(":app:platform:android")
include(":app:platform:ios")
include(":app:platform:common")

include(":app:core:diary-service")
include(":app:core:diary-database")
include(":app:core:diary-database-memory")
include(":app:core:diary-database-room")

include(":app:core:account-preferences")
include(":app:core:account-preferences-memory")
include(":app:core:account-preferences-datastore")

include(":app:core:holiday-service")
include(":app:core:holiday-database")
include(":app:core:holiday-database-memory")
include(":app:core:holiday-database-room")
include(":app:core:holiday-preferences")
include(":app:core:holiday-preferences-memory")
include(":app:core:holiday-preferences-datastore")

include(":app:core:calendar-compose")
include(":app:core:design-system")

include(":app:core:coroutines")
include(":app:core:model")
include(":app:core:navigation")
include(":app:core:resources")

include(":app:data:memo")
include(":app:data:account")
include(":app:data:holiday")
include(":app:data:backup")
include(":app:data:fetch")

include(":app:domain:memo")
include(":app:domain:account")
include(":app:domain:holiday")
include(":app:domain:backup")
include(":app:domain:fetch")

include(":app:feature:memo")
include(":app:feature:calendar")
include(":app:feature:more")
include(":app:feature:account")

include(":server:core:database")
include(":server:core:model")

include(":server:data:account")
include(":server:data:memo")

include(":server:domain:account")
include(":server:domain:memo")

include(":server:app")
include(":server:feature:home")
include(":server:feature:account")
include(":server:feature:memo")

include(":common:exception")
include(":common:model")

include(":library:color")
include(":library:coroutines")
include(":library:datetime")
include(":library:koin-datastore")
include(":library:koin-room")
include(":library:kotlin")
include(":library:navigation")
include(":library:room")
include(":library:shimmer-m3")
