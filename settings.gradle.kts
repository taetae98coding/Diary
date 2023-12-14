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

include(":target:androidApp")
include(":target:iosApp")
include(":target:jvmApp")
include(":target:jsApp")

include(":core:auth-api")
include(":core:auth-impl")
include(":core:auth-koin")

include(":data:repository")

include(":app")

include(":feature:memo")
include(":feature:more")
include(":feature:account")

include(":domain:entity")
include(":domain:repository")
include(":domain:usecase")

include(":navigation:core")

include(":ui:compose")
include(":ui:decompose-compose")

include(":library:firebase-auth-api")
include(":library:firebase-auth-impl")
include(":library:google-sign-api")
include(":library:google-sign-impl")
include(":library:google-sign-compose")
include(":library:viewmodel")
include(":library:koin-navigation-compose")
