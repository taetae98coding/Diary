plugins {
    `kotlin-dsl`
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.gradle.kotlin)
    compileOnly(libs.gradle.ksp)
    compileOnly(libs.gradle.android)
    compileOnly(libs.gradle.compose)
    compileOnly(libs.gradle.compose.compiler)
    compileOnly(libs.gradle.room)
}

gradlePlugin {
    plugins {
        register("diary.kotlin.multiplatform") {
            id = "diary.kotlin.multiplatform"
            implementationClass = "plugin.kotlin.KotlinMultiplatformPlugin"
        }

        register("diary.kotlin.multiplatform.common") {
            id = "diary.kotlin.multiplatform.common"
            implementationClass = "plugin.kotlin.KotlinMultiplatformCommonPlugin"
        }

        register("diary.kotlin.multiplatform.all") {
            id = "diary.kotlin.multiplatform.all"
            implementationClass = "plugin.kotlin.KotlinMultiplatformAllPlugin"
        }

        register("diary.kotlin.jvm") {
            id = "diary.kotlin.jvm"
            implementationClass = "plugin.kotlin.KotlinJvmPlugin"
        }

        register("diary.kotlin.android") {
            id = "diary.kotlin.android"
            implementationClass = "plugin.kotlin.KotlinAndroidPlugin"
        }

        register("diary.android.application") {
            id = "diary.android.application"
            implementationClass = "plugin.android.AndroidApplicationPlugin"
        }

        register("diary.android.library") {
            id = "diary.android.library"
            implementationClass = "plugin.android.AndroidLibraryPlugin"
        }

        register("diary.koin.common") {
            id = "diary.koin.common"
            implementationClass = "plugin.koin.KoinCommonPlugin"
        }

        register("diary.koin.all") {
            id = "diary.koin.all"
            implementationClass = "plugin.koin.KoinAllPlugin"
        }

        register("diary.koin.datastore") {
            id = "diary.koin.datastore"
            implementationClass = "plugin.koin.KoinDataStorePlugin"
        }

        register("diary.koin.room") {
            id = "diary.koin.room"
            implementationClass = "plugin.koin.KoinRoomPlugin"
        }

        register("diary.datastore") {
            id = "diary.datastore"
            implementationClass = "plugin.datastore.DataStorePlugin"
        }

        register("diary.room") {
            id = "diary.room"
            implementationClass = "plugin.room.RoomPlugin"
        }

        register("diary.compose") {
            id = "diary.compose"
            implementationClass = "plugin.compose.ComposePlugin"
        }

        register("diary.app.data") {
            id = "diary.app.data"
            implementationClass = "plugin.convention.AppDataPlugin"
        }

        register("diary.app.domain") {
            id = "diary.app.domain"
            implementationClass = "plugin.convention.AppDomainPlugin"
        }

        register("diary.app.feature") {
            id = "diary.app.feature"
            implementationClass = "plugin.convention.AppFeaturePlugin"
        }

        register("diary.server.domain") {
            id = "diary.server.domain"
            implementationClass = "plugin.convention.ServerDomainPlugin"
        }

        register("diary.server.data") {
            id = "diary.server.data"
            implementationClass = "plugin.convention.ServerDataPlugin"
        }

        register("diary.server.feature") {
            id = "diary.server.feature"
            implementationClass = "plugin.convention.ServerFeaturePlugin"
        }
    }
}
