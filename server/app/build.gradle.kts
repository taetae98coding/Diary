plugins {
    id("diary.kotlin.jvm")
    alias(libs.plugins.ktor.server)
    alias(libs.plugins.dependency.guard)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(project(":server:core:database"))

    implementation(project(":server:data:account"))
    implementation(project(":server:data:memo"))

    implementation(project(":server:domain:account"))
    implementation(project(":server:domain:memo"))

    implementation(project(":server:feature:home"))
    implementation(project(":server:feature:account"))
    implementation(project(":server:feature:memo"))

    implementation(project(":common:model"))

    implementation(libs.bundles.ktor.server)
    implementation(libs.logback.classic)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.ktor)

    implementation(platform(libs.exposed.bom))
    implementation(libs.exposed.jdbc)
    implementation(libs.mysql.connector)
}

dependencyGuard {
    configuration("runtimeClasspath")
}
