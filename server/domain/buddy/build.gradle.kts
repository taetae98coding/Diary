plugins {
    id("diary.server.domain")
}

dependencies {
    implementation(project(":server:domain:fcm"))
    implementation(project(":server:domain:account"))
}
