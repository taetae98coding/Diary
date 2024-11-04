package io.github.taetae98coding.diary.initializer

internal fun intiJs() {
    initAppLifecycleOwner()
    val koinApplication = initKoin()

    initBackupManager(koinApplication)
    initFetchManager(koinApplication)
}
