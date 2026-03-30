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
include(":core:holiday-database:api")
include(":core:holiday-database:impl")
include(":core:holiday-network:api")
include(":core:holiday-network:impl")
include(":core:ip-network:api")
include(":core:ip-network:impl")
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
include(":core:weather-network:api")
include(":core:weather-network:impl")
include(":data:account")
include(":data:credentials")
include(":data:holiday")
include(":data:memo")
include(":data:sync")
include(":data:tag")
include(":data:weather")
include(":domain:account")
include(":domain:credentials")
include(":domain:holiday")
include(":domain:memo")
include(":domain:sync")
include(":domain:tag")
include(":domain:weather")
include(":presenter:calendar")
include(":presenter:memo")
include(":presenter:tag")
include(":feature:calendar")
include(":feature:login")
include(":feature:memo")
include(":feature:more")
include(":feature:tag")
include(":library:kotlinx-file")
include(":library:paging-common")
include(":library:room-common")
include(":library:compose-ui")
include(":library:koin-compose")
include(":library:kotlinx-coroutines-core")
include(":library:kotlinx-datetime")
include(":library:navigation3-runtime")
