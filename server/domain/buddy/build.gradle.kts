plugins {
    id("diary.server.domain")
}

dependencies {
    implementation(project(":server:domain:fcm"))
}
