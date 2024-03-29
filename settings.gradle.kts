pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com.android.*")
                includeGroupByRegex("com.google.*")
                includeGroupByRegex("android.*")
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
                includeGroupByRegex("android.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "Diary"

include(":target:androidApp")
include(":target:iosApp")
include(":target:jvmApp")
include(":target:jsApp")

include(":core:coroutines")
include(":core:auth-api")
include(":core:auth-impl")
include(":core:auth-module")
include(":core:firestore-module")

include(":data:dto")
include(":data:pref-api")
include(":data:pref-impl")
include(":data:local-api")
include(":data:local-holiday")
include(":data:local-impl")
include(":data:remote-api")
include(":data:remote-impl")
include(":data:repository")

include(":app")

include(":feature:memo")
include(":feature:more")
include(":feature:account")
include(":feature:calendar")
include(":feature:tag")
include(":feature:finished-memo")

include(":domain:exception")
include(":domain:entity")
include(":domain:repository")
include(":domain:usecase")

include(":navigation:core")

include(":ui:compose")
include(":ui:entity")
include(":ui:memo-compose")

include(":library:compose-backhandler")
include(":library:compose-calendar")
include(":library:compose-color")
include(":library:compose-runtime")
include(":library:firebase-auth-api")
include(":library:firebase-auth-impl")
include(":library:firebase-firestore-api")
include(":library:firebase-firestore-impl")
include(":library:google-sign-api")
include(":library:google-sign-impl")
include(":library:google-sign-compose")
include(":library:koin-navigation-compose")
include(":library:uuid")
include(":library:kotlin-ext")
include(":library:paging")
include(":library:viewmodel")
