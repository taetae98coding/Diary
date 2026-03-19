plugins {
    `kotlin-dsl`
}

kotlin {
    explicitApi()
}

dependencies {
    compileOnly(libs.buildlogic.kotlin)
    compileOnly(libs.buildlogic.kotlin.compose)
    compileOnly(libs.buildlogic.android)
    compileOnly(libs.buildlogic.jetbrains.compose)
    compileOnly(libs.buildlogic.koin.compiler)
}

gradlePlugin {
    plugins {
        register("diary.convention.data") {
            id = "diary.convention.data"
            implementationClass = "plugin.convention.DataConventionPlugin"
        }
        register("diary.convention.domain") {
            id = "diary.convention.domain"
            implementationClass = "plugin.convention.DomainConventionPlugin"
        }
        register("diary.convention.feature") {
            id = "diary.convention.feature"
            implementationClass = "plugin.convention.FeatureConventionPlugin"
        }
        register("diary.primitive.android.application") {
            id = "diary.primitive.android.application"
            implementationClass = "plugin.primitive.AndroidApplicationPlugin"
        }
        register("diary.primitive.android.library") {
            id = "diary.primitive.android.library"
            implementationClass = "plugin.primitive.AndroidLibraryPrimitivePlugin"
        }
        register("diary.primitive.ios") {
            id = "diary.primitive.ios"
            implementationClass = "plugin.primitive.IosPrimitivePlugin"
        }
        register("diary.primitive.jvm") {
            id = "diary.primitive.jvm"
            implementationClass = "plugin.primitive.JvmPrimitivePlugin"
        }
        register("diary.primitive.wasm") {
            id = "diary.primitive.wasm"
            implementationClass = "plugin.primitive.WasmPrimitivePlugin"
        }
        register("diary.primitive.multiplatform") {
            id = "diary.primitive.multiplatform"
            implementationClass = "plugin.primitive.MultiplatformPrimitivePlugin"
        }
        register("diary.primitive.multiplatform.android") {
            id = "diary.primitive.multiplatform.android"
            implementationClass = "plugin.primitive.AndroidMultiplatformLibraryPrimitivePlugin"
        }
        register("diary.primitive.compose") {
            id = "diary.primitive.compose"
            implementationClass = "plugin.primitive.ComposePrimitivePlugin"
        }
        register("diary.primitive.kotlin") {
            id = "diary.primitive.kotlin"
            implementationClass = "plugin.primitive.KotlinPrimitivePlugin"
        }
        register("diary.primitive.koin") {
            id = "diary.primitive.koin"
            implementationClass = "plugin.primitive.KoinPrimitivePlugin"
        }
        register("diary.primitive.android.test") {
            id = "diary.primitive.android.test"
            implementationClass = "plugin.primitive.AndroidTestPrimitivePlugin"
        }
        register("diary.primitive.android.compose.test") {
            id = "diary.primitive.android.compose.test"
            implementationClass = "plugin.primitive.AndroidComposeTestPrimitivePlugin"
        }
        register("diary.primitive.jvm.test") {
            id = "diary.primitive.jvm.test"
            implementationClass = "plugin.primitive.JvmTestPrimitivePlugin"
        }
    }
}
