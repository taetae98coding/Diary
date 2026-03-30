package io.github.taetae98coding.diary.library.file

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
public fun NSFileManager.Companion.documentDirectory(
    appropriateForURL: NSURL? = null,
    create: Boolean = false,
    error: CPointer<ObjCObjectVar<NSError?>>? = null,
): String? {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = appropriateForURL,
        create = create,
        error = error,
    )

    return documentDirectory?.path
}
