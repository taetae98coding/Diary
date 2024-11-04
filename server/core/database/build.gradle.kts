plugins {
    id("diary.kotlin.jvm")
}

dependencies {
    implementation(project(":server:core:model"))

    implementation(platform(libs.exposed.bom))
    implementation(libs.exposed.core)
    implementation(libs.exposed.datetime)
}
