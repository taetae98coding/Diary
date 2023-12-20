package com.taetae98.diary.library.uuid

import java.util.UUID

public actual fun getUuid(): String {
    return UUID.randomUUID().toString()
}