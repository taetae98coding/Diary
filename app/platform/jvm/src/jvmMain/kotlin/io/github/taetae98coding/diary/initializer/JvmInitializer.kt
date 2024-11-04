package io.github.taetae98coding.diary.initializer

internal suspend fun intiJvm() {
    initAppLifecycleOwner()
    val koinApplication = initKoin()

    initBackupManager(koinApplication)
    initFetchManager(koinApplication)
}
