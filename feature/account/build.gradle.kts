import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

            dependencies {
                implementation(project(":domain:usecase"))
                implementation(project(":navigation:core"))
                implementation(project(":ui:compose"))
                implementation(project(":ui:decompose-compose"))
                implementation(project(":library:google-auth-compose"))
                implementation(project(":library:viewmodel"))

                implementation(compose.material3)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(project.dependencies.platform(libs.koin.annotations.bom))
                implementation(libs.koin.annotations)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.account"
}

dependencies {
    kspCommonMainMetadata(platform(libs.koin.annotations.bom))
    kspCommonMainMetadata(libs.koin.compiler)
}

tasks.withType<KotlinCompile<*>>().all {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}