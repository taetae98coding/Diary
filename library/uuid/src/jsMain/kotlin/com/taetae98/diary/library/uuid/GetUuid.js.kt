package com.taetae98.diary.library.uuid

import kotlinx.datetime.Clock

public actual fun getUuid(): String {
    return Clock.System.now().toString()
}
