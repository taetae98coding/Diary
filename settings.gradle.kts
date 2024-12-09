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
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "Diary"

include(":app:platform:jvm")
include(":app:platform:android")
include(":app:platform:ios")
//include(":app:platform:macos")
include(":app:platform:common")

include(":app:core:diary-service")
include(":app:core:diary-database")
include(":app:core:diary-database-room")

include(":app:core:account-preferences")
include(":app:core:account-preferences-datastore")

include(":app:core:holiday-service")
include(":app:core:holiday-database")
include(":app:core:holiday-database-room")
include(":app:core:holiday-preferences")
include(":app:core:holiday-preferences-datastore")

include(":app:core:backup-database")
include(":app:core:backup-database-room")

include(":app:core:filter-database")
include(":app:core:filter-database-room")

include(":app:core:compose")
include(":app:core:calendar-compose")
include(":app:core:design-system")

include(":app:core:coroutines")
include(":app:core:model")
include(":app:core:navigation")

include(":app:data:memo")
include(":app:data:tag")
include(":app:data:account")
include(":app:data:holiday")
include(":app:data:backup")
include(":app:data:fetch")
include(":app:data:fcm")
include(":app:data:credential")
include(":app:data:calendar")
include(":app:data:buddy")

include(":app:domain:memo")
include(":app:domain:tag")
include(":app:domain:calendar")
include(":app:domain:buddy")
include(":app:domain:account")
include(":app:domain:holiday")
include(":app:domain:backup")
include(":app:domain:fetch")
include(":app:domain:fcm")
include(":app:domain:credential")

include(":app:feature:memo")
include(":app:feature:tag")
include(":app:feature:calendar")
include(":app:feature:buddy")
include(":app:feature:more")
include(":app:feature:account")

include(":server:core:database")
include(":server:core:model")

include(":server:data:account")
include(":server:data:memo")
include(":server:data:tag")
include(":server:data:fcm")
include(":server:data:buddy")

include(":server:domain:account")
include(":server:domain:memo")
include(":server:domain:tag")
include(":server:domain:fcm")
include(":server:domain:buddy")

include(":server:app")
include(":server:feature:home")
include(":server:feature:account")
include(":server:feature:memo")
include(":server:feature:tag")
include(":server:feature:fcm")
include(":server:feature:buddy")

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

include(":library:firebase-common")
include(":library:firebase-messaging")
