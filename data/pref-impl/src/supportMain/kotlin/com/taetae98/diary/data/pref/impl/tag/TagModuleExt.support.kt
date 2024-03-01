package com.taetae98.diary.data.pref.impl.tag

import com.taetae98.diary.data.pref.api.TagPrefDataSource
import com.taetae98.diary.data.pref.impl.ext.getDataSource

internal actual fun TagModule.getTagPrefDataSource(): TagPrefDataSource {
    return TagPrefDataSourceImpl(
        dataStore = getDataSource("tag_datastore.preferences_pb"),
    )
}
