package com.taetae98.diary.data.pref.impl.tag

import com.taetae98.diary.data.pref.api.TagPrefDataSource

internal actual fun TagModule.getTagPrefDataSource(): TagPrefDataSource {
    return TagPrefDataSourceImpl()
}
