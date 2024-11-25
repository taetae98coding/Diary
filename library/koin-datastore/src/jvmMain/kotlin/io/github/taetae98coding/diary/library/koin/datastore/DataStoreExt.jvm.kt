package io.github.taetae98coding.diary.library.koin.datastore

import org.koin.core.component.KoinComponent
import java.io.File

public var koinDataStoreDefaultPath: String = System.getProperty("user.home")

internal actual fun KoinComponent.getDataStoreAbsolutePath(name: String): String {
	runCatching { File("$koinDataStoreDefaultPath/$name").parentFile?.mkdirs() }
	return "$koinDataStoreDefaultPath/$name"
}
