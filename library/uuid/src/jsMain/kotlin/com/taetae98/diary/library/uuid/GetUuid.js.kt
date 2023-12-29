package com.taetae98.diary.library.uuid

import com.benasher44.uuid.Uuid

public actual fun getUuid(): String {
    return Uuid.toString()
}
