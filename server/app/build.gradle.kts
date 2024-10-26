plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor.server)
    alias(libs.plugins.dependency.guard)
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(project(":server:feature:home"))

    implementation(libs.bundles.ktor.server)
    implementation(libs.logback.classic)
}

dependencyGuard {
    configuration("runtimeClasspath") {
        tree = true
    }
}