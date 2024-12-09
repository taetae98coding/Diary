package io.github.taetae98coding.diary.library.koin.datastore

import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.component.KoinComponent
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
internal actual fun KoinComponent.getDataStoreAbsolutePath(name: String): String {
	val documentDirectory: NSURL? =
		NSFileManager.defaultManager
			.URLForDirectory(
				directory = NSDocumentDirectory,
				inDomain = NSUserDomainMask,
				appropriateForURL = null,
				create = false,
				error = null,
			)?.also {
//        NSFileManager.defaultManager.removeItemAtURL(it, null)
			}

	return requireNotNull(documentDirectory).path + "/$name"
}
