package com.taetae98.diary.data.pref.impl.select.tag

import com.taetae98.diary.data.pref.api.SelectTagByMemoPrefDataSource
import com.taetae98.diary.data.pref.impl.ext.getDataSource

internal actual fun SelectTagModule.getSelectTagByMemoPrefDataSource(): SelectTagByMemoPrefDataSource {
    return SelectTagByMemoPrefDataSourceImpl(
        dataStore = getDataSource("select_tag_by_memo_datastore.preferences_pb")
    )
}