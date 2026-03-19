rootProject.name = "Diary"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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

include(":app:shared")
include(":app:android")
include(":app:jvm")
include(":app:ios")
include(":app:wasm")
include(":compose:core")
include(":compose:calendar")
include(":core:database:api")
include(":core:database:impl")
include(":core:datastore:api")
include(":core:datastore:impl")
include(":core:network:api")
include(":core:network:impl")
include(":core:supabase:api")
include(":core:supabase:impl")
include(":core:google-credentials:api")
include(":core:google-credentials:compose")
include(":core:google-credentials:impl")
include(":core:mapper")
include(":core:model")
include(":core:navigation")
include(":data:account")
include(":data:credentials")
include(":data:memo")
include(":data:sync")
include(":domain:account")
include(":domain:credentials")
include(":domain:memo")
include(":domain:sync")
include(":presenter:calendar:api")
include(":presenter:calendar:compose")
include(":presenter:memo:api")
include(":presenter:memo:compose")
include(":feature:calendar")
include(":feature:login")
include(":feature:memo")
include(":feature:more")
include(":library:room-common")
include(":library:compose-ui")
include(":library:koin-compose")
include(":library:kotlinx-coroutines-core")
include(":library:kotlinx-datetime")
include(":library:navigation3-runtime")
