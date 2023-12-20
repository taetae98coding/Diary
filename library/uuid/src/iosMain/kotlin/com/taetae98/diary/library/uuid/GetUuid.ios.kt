package com.taetae98.diary.library.uuid

import platform.Foundation.NSUUID

public actual fun getUuid(): String {
    return NSUUID().UUIDString()
}